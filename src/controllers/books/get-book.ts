import { Request, Response, NextFunction } from 'express';
import { getBookById } from '../../services/books/book-service';

export async function getBookController(
  req: Request,
  res: Response,
  next: NextFunction,
) {
  try {
    const userId = req.userId as number;
    const id = Number.parseInt(req.params.id);

    const book = await getBookById(userId, id);

    if (!book) {
      return res.status(404).json({ message: 'Livro não encontrado' });
    }

    res.json(book);
  } catch (err) {
    next(err);
  }
}
