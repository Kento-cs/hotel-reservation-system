# hotel_master

ホテル予約システム（HRS: Hotel Reservation System）の設計・実装リポジトリ。
大学のソフトウェア工学の授業で、4名のチームで要求分析から設計・実装・保守まで行ったものです。

> このリポジトリは、チームの非公開リポジトリから自分のポートフォリオ用に複製したものです。授業の配布資料と、授業で配られたサンプル実装（動作に必要な日付ユーティリティ `util.DateUtil` を除く）は除いています。

## 概要

宿泊客がブラウザで空室を検索・予約し、ホテルの管理者がチェックイン・チェックアウトや部屋の管理を行うWebアプリケーションです。
フレームワークを使わずに、JDK標準の `HttpServer` でREST風のAPIを作り、素のHTML/JavaScriptで画面を作っています。

<!-- スクリーンショットを追加する場合: docs/screenshots/ に画像を置き、下の行のコメントを外す -->
<!-- ![予約画面](docs/screenshots/index.png) ![管理者画面](docs/screenshots/admin.png) -->

### 主な機能

| 宿泊客向け（`/`） | 管理者向け（`/admin`、パスワード認証あり） |
| --- | --- |
| 部屋種別・価格帯・日付での空室検索 | チェックイン／チェックアウト（料金計算つき） |
| 月表示の空室カレンダー | 部屋情報の登録・一覧・編集 |
| 予約（確認画面・完了画面つき） | 宿泊者一覧 |
| 予約の確認・キャンセル | |

### 使用技術

- **言語**: Java（JDK 18以上）、HTML / CSS / JavaScript
- **サーバー**: JDK標準 `com.sun.net.httpserver.HttpServer`（外部フレームワークなし）
- **DB**: HSQLDB（埋め込みファイルモード）、DAOパターンで永続化
- **設計**: Astahによるユースケース図・ドメインモデル・BCE（Boundary / Control / Entity）パターンでのアーキテクチャ設計
- **開発**: Git / GitHub（ブランチとプルリクエストでのチーム開発）

### 開発体制・期間

- 4名のチーム開発、2026年6月〜7月
- 流れ: 要求分析 → ドメイン分析 → システム分析 → アーキテクチャ設計 → 実装 → 保守（「予約をキャンセルする」ユースケースの追加）

### 自分（中西）の担当

- **リポジトリの作成と管理**
- **ドメイン分析**: ドメインモデル（`ドメイン分析 (1).asta`）と方針ドキュメント（`ドメイン分析方針.md`）の作成
- **管理者ページの分離**: 宿泊客向けページ（`static/index.html`）から管理者の機能を切り出し、管理者専用ページ（`static/admin.html`）を新しく作成
- **宿泊者一覧の追加**: `OccupancyManager` / `OccupancyHandler`（`/api/occupancy`）を実装
- **空室数のバグ修正**: チェックアウト済みの予約まで「使用中」として数えていたため空室数が実際より少なく表示されていた問題を、`DataStore` の計算から除外して修正
- **部屋種別の整理**: スタンダード／スイート × 喫煙／禁煙の4種別・30室の初期データに整理
- **入力バリデーション**: 部屋登録（部屋番号・愛称の文字数、価格の範囲）と予約（氏名・連絡先の文字数、人数が定員内か）を、DBのスキーマ制約に合わせてサーバー側でチェックし、400エラーを返すようにした（`web.Validation`）
- **空室カレンダー**: 部屋種別と月を指定して日ごとの空室数を返すAPI（`/api/availability`）と、検索画面の月表示カレンダーを実装（日付をクリックすると宿泊日の欄に入る）
- **部屋一覧・編集**: 管理者ページに部屋一覧・編集タブを追加（`/api/rooms/list`、`/api/rooms/edit`）
- **管理者パスワードの外部化**: パスワードを `.env` から読み込む仕組み（`Env`、`.env.example`）を追加

## 起動方法（すぐ試す場合）

JDKをインストールした Windows で `run.bat` をダブルクリックすると、コンパイルとサーバーの起動を行い、ブラウザで `http://localhost:8080/` を開きます。
## 構成

- `*.asta` / `*方針.md` : Astah による要求分析・システム分析・アーキテクチャ設計・ドメイン分析のモデルと、その内容をまとめた方針ドキュメント
- `use-cases_1.md` : ユースケース定義
- `src/main/java` : Java実装
  - `userinterface` / `application` : 自チームのアーキテクチャ設計（アーキテクチャ設計.md）に基づく実装。BCEパターンで、`userinterface`にCUI用のバウンダリ（画面）クラス、`application`に管理（コントロール）クラスとエンティティクラスを配置。7ユースケース（部屋を検索する／予約する／確認する、チェックインする、チェックアウトする、部屋情報を登録する、予約をキャンセルする）に対応
    - `application.dao` : HSQLDB（埋め込みファイルモード、`db/hrsdb`）への永続化を担うDAO層。`DataStore`はここを経由してDBを読み書きする（非機能要件「永続性：提供される部分実装(HSQLDB)に従うこと」への対応）
  - `web` : 上記`application`層をJDK標準の`HttpServer`でAPI化し、ブラウザ（`static/index.html`）から操作できるようにしたWeb版
  - `util` : 授業で配布されたサンプル実装に含まれる日付ユーティリティ（`DateUtil`）。著作権表示はファイル内のとおり
- `static/index.html` / `static/admin.html` : Web版のフロントエンド（宿泊客向け／管理者向け。`web.HrsHttpServer`が配信する）
- `lib/hsqldb.jar` : HSQLDBドライバ
- `db/` : `application.dao`が使う`hrsdb.*`の置き場所。初回起動時に自動生成される（Git管理対象外）

## Web版の動かし方（Windows・ブラウザで表示）

リポジトリのルートディレクトリで実行する（`static/index.html`と`db/hrsdb`を相対パスで読むため）。

```
javac -encoding UTF-8 -cp lib/hsqldb.jar -d bin -sourcepath src/main/java src/main/java/web/HrsHttpServer.java
java -Dstdout.encoding=UTF-8 -cp bin;lib/hsqldb.jar web.HrsHttpServer
```

起動後、ブラウザで `http://localhost:8080/` を開く。画面上部のタブから、部屋の検索・予約、予約確認を行える。データはHSQLDB（`db/hrsdb.*`）に保存され、サーバーを再起動しても引き継がれる。初期状態に戻したい場合は`db/hrsdb.*`を削除してから再起動する。

チェックイン・チェックアウト・部屋情報の登録（いずれもHRS管理者操作）は、顧客向けページとは分離した管理者用ページ `http://localhost:8080/admin` から行う。アクセス時に管理者パスワード（既定値: `password`）の入力を求められる。

## CUI版の動かし方（Windows）

```
javac -encoding UTF-8 -cp lib/hsqldb.jar -d bin -sourcepath src/main/java src/main/java/userinterface/MainMenu.java
java -Dstdout.encoding=UTF-8 -cp bin;lib/hsqldb.jar userinterface.MainMenu
```

CUIのメニューから、Web版と同じ7ユースケースを操作できる（`application`層とDBを共有）。`-Dstdout.encoding=UTF-8`はWindowsコンソールでの日本語文字化けを防ぐためのオプション。
