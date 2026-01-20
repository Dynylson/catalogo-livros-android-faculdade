import 'dotenv/config';
import express from 'express';
import cors from 'cors';
import bookRoutes from './routes/books/routes';

const app = express();
const port = process.env.PORT || 3000;

app.use(cors());
app.use(express.json());

app.use('/api/books', bookRoutes);

app.listen(port, () => {
  console.log(`Server running on port ${port}`);
});
