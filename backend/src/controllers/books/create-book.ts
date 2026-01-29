import { Request, Response, NextFunction } from 'express';
import { createBook } from '../../services/books/book-service';
import { createBookSchema } from '../../utils/validation';

export async function createBookController(
  req: Request,
  res: Response,
  next: NextFunction,
) {
  try {
    const payload = createBookSchema.parse(req.body);
    const book = await createBook(req.userId!, payload);
    res.status(201).json(book);
  } catch (err) {
    next(err);
  }
}
