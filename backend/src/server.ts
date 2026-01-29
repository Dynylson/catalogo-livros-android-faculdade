import 'dotenv/config';
import express from 'express';
import cors from 'cors';
import bookRoutes from './routes/books/routes';
import authRoutes from './routes/auth/routes';
import { errorHandler } from './middlewares/error-handler';
import { authMiddleware } from './middlewares/auth';

const app = express();
const port = process.env.PORT || 3000;

app.use(cors());
app.use(express.json());

app.use('/api/auth', authRoutes);
app.use('/api/books', authMiddleware, bookRoutes);
app.use(errorHandler);

app.listen(port, () => {
  console.log(`Server running on port ${port}`);
});
