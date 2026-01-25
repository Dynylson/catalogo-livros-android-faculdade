import { PrismaClient } from '@prisma/client';
import bcrypt from 'bcrypt';
import { MOCK_USER_ID } from '../src/config/mock-user';

const prisma = new PrismaClient();

async function main() {
  const email = 'teste@teste.com';
  const password = '1234';
  const name = 'Usuário Teste';

  const existing = await prisma.user.findUnique({ where: { email } });

  if (!existing) {
    const hash = await bcrypt.hash(password, 10);
    await prisma.user.create({
      data: {
        id: MOCK_USER_ID,
        email,
        password: hash,
        name,
      },
    });
  }
}

main()
  .catch((e) => {
    console.error(e);
    process.exit(1);
  })
  .finally(async () => {
    await prisma.$disconnect();
  });
