# ベースイメージ
FROM node:16

# 作業ディレクトリの設定
WORKDIR /app

# パッケージファイルとその他必要なファイルをコピー
COPY api-stub-server/package.json ./
COPY api-stub-server/tsconfig.json ./
COPY api-stub-server/src/ ./src
#COPY api-spec/api-spec.yaml ./api-spec.yaml

# 依存関係のインストール
RUN npm install

# TypeScriptファイルをビルド
RUN npx tsc

# コンテナを外部と接続するためのポートを公開
EXPOSE 3000

# アプリケーションを実行
CMD ["node", "dist/index.js"]
