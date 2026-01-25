import { Router } from 'express';
import {
  listBooksController,
  getBookController,
  createBookController,
  updateBookController,
  deleteBookController,
} from '../../controllers/books';

const router = Router();

router.get('/', listBooksController);
router.get('/:id', getBookController);
router.post('/', createBookController);
router.put('/:id', updateBookController);
router.delete('/:id', deleteBookController);

export default router;
