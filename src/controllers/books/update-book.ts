import { Request, Response, NextFunction } from 'express';
import { updateBook } from '../../services/books/book-service';
import { updateBookSchema } from '../../utils/validation';

export async function updateBookController(
  req: Request,
  res: Response,
  next: NextFunction,
) {
  try {
    const userId = req.userId as number;
    const id = Number.parseInt(req.params.id);

    const payload = updateBookSchema.parse(req.body);

    const book = await updateBook(userId, id, payload);

    if (!book) {
      return res.status(404).json({ message: 'Livro não encontrado' });
    }

    res.json(book);
  } catch (err) {
    next(err);
  }
}
