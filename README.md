# 環境構築手順
以下に環境構築手順を記載します。

## 事前準備
まずはIntelliJ IDEAをインストールし、このプロジェクトを開いてください。

## DBの起動
```bash
docker compose up -d
```

## Spring Bootアプリケーションの起動

### 環境変数の設定
Intellij IDEAの右上にある`Edit Configurations`をクリックし、`Environment`タブを選択して環境変数を設定してください。
または、bashに以下の環境変数を設定してください。(exportコマンドを使用)
```bash
SPRING_APPLICATION_NAME=api-server
SPRING_DATASOURCE_URL=jdbc:postgresql://127.0.0.1:5432/postgres
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=password
SPRING_DATASOURCE_DRIVER_CLASS_NAME=org.postgresql.Driver
```

### 方法1
IntelliJ IDEAで`src/main/java/com/unicorn/api/ApiServerApplication.kt`を開き、Runボタンを押すか、画面右上のRunボタンをクリックしてください。

### 方法2
プロジェクトのルートディレクトリで以下のコマンドを実行してください。
85% EXECUTINGという表示が出れば成功です。
```bash
./gradlew bootRun
```

## 動作確認
ブラウザで `http://localhost:8080/hello` にアクセスし、以下のメッセージが表示されれば成功です。

```
Hello, World!
```

## おまけ
Dockerを用いてSpring Bootアプリケーションを起動する方法もあります。
プロジェクトの`./api-server`で以下のコマンドを実行してください。

```bash
docker build --no-cache -t spring-boot-demo .
```

```bash
docker run -p 8080:8080 spring-boot-demo
```


## [![Repography logo](https://images.repography.com/logo.svg)](https://repography.com) / Recent activity [![Time period](https://images.repography.com/55618960/unicorn-hal/unicorn-monorepo/recent-activity/H_7xXywquq3U0OenWpcF6da-VlNZ-2Wvx22b2oeir4s/nv9DV-2p2yCO9Tpw24Kxvkq-9Fh9qQ0VwOuxDxxlVN4_badge.svg)](https://repography.com)
[![Timeline graph](https://images.repography.com/55618960/unicorn-hal/unicorn-monorepo/recent-activity/H_7xXywquq3U0OenWpcF6da-VlNZ-2Wvx22b2oeir4s/nv9DV-2p2yCO9Tpw24Kxvkq-9Fh9qQ0VwOuxDxxlVN4_timeline.svg)](https://github.com/unicorn-hal/unicorn-monorepo/commits)
[![Issue status graph](https://images.repography.com/55618960/unicorn-hal/unicorn-monorepo/recent-activity/H_7xXywquq3U0OenWpcF6da-VlNZ-2Wvx22b2oeir4s/nv9DV-2p2yCO9Tpw24Kxvkq-9Fh9qQ0VwOuxDxxlVN4_issues.svg)](https://github.com/unicorn-hal/unicorn-monorepo/issues)
[![Pull request status graph](https://images.repography.com/55618960/unicorn-hal/unicorn-monorepo/recent-activity/H_7xXywquq3U0OenWpcF6da-VlNZ-2Wvx22b2oeir4s/nv9DV-2p2yCO9Tpw24Kxvkq-9Fh9qQ0VwOuxDxxlVN4_prs.svg)](https://github.com/unicorn-hal/unicorn-monorepo/pulls)
[![Trending topics](https://images.repography.com/55618960/unicorn-hal/unicorn-monorepo/recent-activity/H_7xXywquq3U0OenWpcF6da-VlNZ-2Wvx22b2oeir4s/nv9DV-2p2yCO9Tpw24Kxvkq-9Fh9qQ0VwOuxDxxlVN4_words.svg)](https://github.com/unicorn-hal/unicorn-monorepo/commits)
[![Top contributors](https://images.repography.com/55618960/unicorn-hal/unicorn-monorepo/recent-activity/H_7xXywquq3U0OenWpcF6da-VlNZ-2Wvx22b2oeir4s/nv9DV-2p2yCO9Tpw24Kxvkq-9Fh9qQ0VwOuxDxxlVN4_users.svg)](https://github.com/unicorn-hal/unicorn-monorepo/graphs/contributors)
[![Activity map](https://images.repography.com/55618960/unicorn-hal/unicorn-monorepo/recent-activity/H_7xXywquq3U0OenWpcF6da-VlNZ-2Wvx22b2oeir4s/nv9DV-2p2yCO9Tpw24Kxvkq-9Fh9qQ0VwOuxDxxlVN4_map.svg)](https://github.com/unicorn-hal/unicorn-monorepo/commits)

