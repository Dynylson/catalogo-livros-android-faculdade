import prisma from '../../prisma/client';

export type CreateBookInput = {
  title: string;
  author: string;
  year: number;
  genre: string;
  status: 'READ' | 'READING' | 'WANT_TO_READ';
  rating?: number;
  review?: string;
};

export type UpdateBookInput = Partial<CreateBookInput>;

export async function listBooks(userId: number) {
  return prisma.book.findMany({ where: { userId } });
}

export async function getBookById(userId: number, id: number) {
  return prisma.book.findFirst({ where: { id, userId } });
}

export async function createBook(userId: number, data: CreateBookInput) {
  return prisma.book.create({ data: { ...data, userId } });
}

export async function updateBook(
  userId: number,
  id: number,
  data: UpdateBookInput,
) {
  const existing = await prisma.book.findFirst({ where: { id, userId } });

  if (!existing) return null;

  return prisma.book.update({ where: { id }, data });
}

export async function deleteBook(userId: number, id: number) {
  const existing = await prisma.book.findFirst({ where: { id, userId } });

  if (!existing) return false;

  await prisma.book.delete({ where: { id } });

  return true;
}
