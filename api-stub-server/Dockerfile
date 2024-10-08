FROM node:16

# 作業ディレクトリを設定
WORKDIR /usr/src/app

# package.json と package-lock.json をコピー
COPY package*.json ./

# 依存関係をインストール
RUN npm install

# アプリケーションのソースコードをコピー
COPY . .

# 環境変数 PORT を設定
ENV PORT 8080

# ポートを公開
EXPOSE 8080

# アプリケーションを起動
CMD ["node", "index.js"]
