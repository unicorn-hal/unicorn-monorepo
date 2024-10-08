FROM node:16

# 作業ディレクトリを設定
WORKDIR /usr/src/app

# package.json と package-lock.json をコピー
COPY api-stub-server/package*.json ./

# 依存関係をインストール
RUN npm install

# アプリケーションのソースコードをコピー
COPY . .

# アプリケーションをビルド (必要な場合)
# RUN npm run build

# Cloud Run がリッスンするポートを指定
ENV PORT=8080

# コンテナ起動時に実行するコマンド
CMD node index.js

# Expose port 3000
EXPOSE 8080