import { z } from 'zod';

export const bookStatusEnum = z.enum(['READ', 'READING', 'WANT_TO_READ']);

export const createBookSchema = z.object({
  title: z.string().min(1, 'Título é obrigatório'),
  author: z.string().min(1, 'Autor é obrigatório'),
  year: z.number().int().min(0, 'Ano deve ser positivo'),
  genre: z.string().min(1, 'Gênero é obrigatório'),
  status: bookStatusEnum,
  rating: z.number().int().min(1).max(5).optional(),
  review: z.string().optional(),
});

export const updateBookSchema = createBookSchema
  .partial()
  .refine((data) => Object.keys(data).length > 0, {
    message: 'Forneça pelo menos um campo para atualizar',
  });
