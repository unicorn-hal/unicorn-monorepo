import express from 'express';
import swaggerJsdoc from 'swagger-jsdoc';
import path from 'path';
import fs from 'fs';

const swaggerUi = require("swagger-ui-express")

const apiSpecPath = path.resolve(__dirname, '../api-spec/api-spec.yaml');
const apiSpec = fs.readFileSync(apiSpecPath, 'utf8');

const options = {
  definition: JSON.parse(apiSpec),
  apis: []
};

const swaggerSpec = swaggerJsdoc(options);

const app = express();
const port = 3000;

app.use('/api-docs', swaggerUi.serve, swaggerUi.setup(swaggerSpec));

app.get('/hello', (req, res) => {
  res.send('Hello, World!');
});

app.listen(port, () => {
  console.log(`Stub server running at http://localhost:${port}`);
});
