import { Request, Response, NextFunction } from 'express';
import { deleteBook } from '../../services/books/book-service';

export async function deleteBookController(
  req: Request,
  res: Response,
  next: NextFunction,
) {
  try {
    const userId = req.userId as number;
    const id = Number.parseInt(req.params.id);

    const deleted = await deleteBook(userId, id);

    if (!deleted) {
      return res.status(404).json({ message: 'Livro não encontrado' });
    }

    res.status(204).send();
  } catch (err) {
    next(err);
  }
}
