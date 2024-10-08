# ベースイメージ
FROM node:16

# 作業ディレクトリの設定
WORKDIR /app

# パッケージファイルをコピー
COPY package.json package-lock.json ./

# 依存関係のインストール
RUN npm install

# アプリケーションファイルをコピー
COPY . .

# コンテナを外部と接続するためのポートを公
# PORT 環境変数を使うようにアプリケーションを設定して実行
CMD ["node", "index.js"]

EXPOSE 3000