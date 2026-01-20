import { Request, Response, NextFunction } from 'express';
import { listBooks } from '../../services/books/book-service';

export async function listBooksController(
  req: Request,
  res: Response,
  next: NextFunction,
) {
  try {
    const userId = req.userId as number;
    const books = await listBooks(userId);
    res.json(books);
  } catch (err) {
    next(err);
  }
}
