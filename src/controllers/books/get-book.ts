import { Request, Response, NextFunction } from 'express';
import { getBookById } from '../../services/books/book-service';
import { MOCK_USER_ID } from '../../config/mock-user';

export async function getBookController(
  req: Request,
  res: Response,
  next: NextFunction,
) {
  try {
    const id = Number.parseInt(req.params.id);

    const book = await getBookById(MOCK_USER_ID, id);

    if (!book) {
      return res.status(404).json({ message: 'Livro não encontrado' });
    }

    res.json(book);
  } catch (err) {
    next(err);
  }
}
