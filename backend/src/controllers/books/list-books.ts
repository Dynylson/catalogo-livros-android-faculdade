import { Request, Response, NextFunction } from 'express';
import { listBooks } from '../../services/books/book-service';
import { MOCK_USER_ID } from '../../config/mock-user';

export async function listBooksController(
  req: Request,
  res: Response,
  next: NextFunction,
) {
  try {
    const books = await listBooks(MOCK_USER_ID);
    res.json(books);
  } catch (err) {
    next(err);
  }
}
