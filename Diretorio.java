import java.io.*;

public class Diretorio 
{
    private int profGlobal;
    private long[] vetDiretorio;

    public Diretorio() 
    {
        profGlobal = 0;
        vetDiretorio = new long[150000];
    }

    public Diretorio(int profGlobal, long[] vetDiretorio) 
    {
        this.profGlobal = profGlobal;

        for (int i = 0; i < Math.pow(2, profGlobal); i++) 
        {
            vetDiretorio[i] = -1;
            this.vetDiretorio[i] = vetDiretorio[i];
        }
    }

    public int getProfGlobal() 
    {
        return profGlobal;
    }

    public void setProfGlobal(int profGlobal) 
    {
        this.profGlobal = profGlobal;
    }

    public long getVetDiretorio(int i) 
    {
        return vetDiretorio[i];
    }

    public void setVetDiretorio(int i, long enderecoI) 
    {
        this.vetDiretorio[i] = enderecoI;
    }

    public boolean cheio() 
    {
        boolean resposta = true;

        for (int i = 0; i < Math.pow(2, profGlobal); i++) 
        {
            if (vetDiretorio[i] == -1) 
            {
                resposta = false;
                break;
            }
        }
        return resposta;
    }

    public void preencheDiretorio(int posVetDiretorio, long posArquivoByte) 
    {
        vetDiretorio[posVetDiretorio] = posArquivoByte;
    }

    public void duplicaDiretorio(int novaProfGlobal, Diretorio Diretorio) 
    {
        Diretorio novoDiretorio = new Diretorio();
        Double atualizaIndice = Math.pow(2, novaProfGlobal) - Math.pow(2, Diretorio.getProfGlobal());
        int atualizaIndiceAt = atualizaIndice.intValue();

        for (int i = 0; i < Math.pow(2, Diretorio.getProfGlobal()); i++) 
        {
            novoDiretorio.vetDiretorio[i] = Diretorio.vetDiretorio[i];
            novoDiretorio.vetDiretorio[atualizaIndiceAt] = Diretorio.vetDiretorio[i];

            if (atualizaIndice == Math.pow(2, novaProfGlobal))
                break;
        }
    }

    public void imprimeDiretorio() 
    {
        System.out.println("Diretório: ");
        System.out.print("[");

        for (int i = 0; i < Math.pow(2, profGlobal); i++) 
        {
            System.out.print("- " + vetDiretorio[i]);
        }

        System.out.print(" -]");
    }

    public int TabelaHash(Diretorio a, int entradaCPF) 
    {
        double funcao = 0;
        int resposta;
        funcao = entradaCPF % Math.pow(2, a.getProfGlobal());
        resposta = (int) funcao;
        return resposta;
    }

    public int newTabelaHash(int a, int b) 
    {
        int resposta = (int) (b % Math.pow(2, a));
        return resposta;
    }

    public byte[] toByteArray() throws IOException 
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        for (int i = 0; i < vetDiretorio.length; i++) 
        {
            dos.writeInt(profGlobal);
            dos.writeLong(vetDiretorio[i]);
            dos.flush();
        }

        return baos.toByteArray();
    }

    public void fromByteArray(byte[] ba) throws IOException 
    {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);

        for (int i = 0; i < vetDiretorio.length; i++) 
        {
            profGlobal = dis.readInt();
            vetDiretorio[i] = dis.readLong();
        }
    }
}
