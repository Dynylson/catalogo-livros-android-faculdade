import { Request, Response, NextFunction } from 'express';
import { listBooks } from '../../services/books/book-service';

export async function listBooksController(
  req: Request,
  res: Response,
  next: NextFunction,
) {
  try {
    const books = await listBooks(req.userId!);
    res.json(books);
  } catch (err) {
    next(err);
  }
}
