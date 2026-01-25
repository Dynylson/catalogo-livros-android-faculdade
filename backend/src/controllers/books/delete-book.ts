import { Request, Response, NextFunction } from 'express';
import { deleteBook } from '../../services/books/book-service';
import { MOCK_USER_ID } from '../../config/mock-user';

export async function deleteBookController(
  req: Request,
  res: Response,
  next: NextFunction,
) {
  try {
    const id = Number.parseInt(req.params.id);

    const deleted = await deleteBook(MOCK_USER_ID, id);

    if (!deleted) {
      return res.status(404).json({ message: 'Livro não encontrado' });
    }

    res.status(204).send();
  } catch (err) {
    next(err);
  }
}
