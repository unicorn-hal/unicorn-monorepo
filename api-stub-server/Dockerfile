FROM node:14

# 作業ディレクトリを設定
WORKDIR /usr/src/app

# package.json と package-lock.json をコピー
COPY package*.json ./

# 依存関係をインストール
RUN npm install

# アプリケーションのソースコードをコピー
COPY . .

# アプリケーションをビルド (必要な場合)
# RUN npm run build

# Cloud Run がリッスンするポートを指定
ENV PORT=3000

# コンテナ起動時に実行するコマンド
CMD [ "node", "index.js" ]

# Expose port 8080
EXPOSE 3000