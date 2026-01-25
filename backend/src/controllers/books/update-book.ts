import { Request, Response, NextFunction } from 'express';
import { updateBook } from '../../services/books/book-service';
import { updateBookSchema } from '../../utils/validation';
import { MOCK_USER_ID } from '../../config/mock-user';

export async function updateBookController(
  req: Request,
  res: Response,
  next: NextFunction,
) {
  try {
    const id = Number.parseInt(req.params.id);

    const payload = updateBookSchema.parse(req.body);

    const book = await updateBook(MOCK_USER_ID, id, payload);

    if (!book) {
      return res.status(404).json({ message: 'Livro não encontrado' });
    }

    res.json(book);
  } catch (err) {
    next(err);
  }
}
