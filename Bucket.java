import java.io.*;

public class Bucket 
{
    private ParPessoa dado[];
    private int quantDadosBucket;
    private int profLocal;

    public Bucket() 
    {}

    public Bucket(int profLocal, int quantDadosBucket, ParPessoa dado[])
    {

        ParPessoa a = new ParPessoa(0, 0);
        dado = new ParPessoa[quantDadosBucket];
        for (int i = 0; i < quantDadosBucket; i++)
        {
            dado[i] = a;
        }

        this.profLocal = 0;


    }

    public int getProfLocal() 
    {
        return getProfLocal();
    }

    public void setProfLocal(int profLocal) 
    {
        this.profLocal = profLocal;
    }

    public int getQuantDadosBucket() 
    {
        return getQuantDadosBucket();
    }

    public void setQuantDadosBucket(int quantDadosBucket) 
    {
        this.quantDadosBucket = quantDadosBucket;
    }

    public int getDadoCPF(int pos, int CPF) 
    {
        return dado[pos].getCPFpessoa();
    }

    public void setDadoCPF(int pos, int CPF)
    {
        dado[pos].setCPFpessoa(CPF);
    }

    public boolean update(Bucket a, ParPessoa b) 
    {
        boolean resposta = true;

        if (cheio(a) == false) 
        {
            for (int i = 0; i < a.quantDadosBucket; i++) 
            {
                if (a.dado[i].getCPFpessoa() == -1) 
                {
                    a.dado[i] = b;
                    resposta = true;
                }
            }
        } 
        else 
        {
            resposta = false;
        }

        return resposta;
    }

    public long read(int CPFPessoa, Bucket a) 
    {
        if (vazio(a)) 
        {
            return -1;
        }

        int i = 0;

        while (i < quantDadosBucket && CPFPessoa > a.getDadoCPF(i, CPFPessoa)) 
        {
            i++;
        }

        if (i < quantDadosBucket && CPFPessoa == a.getDadoCPF(i, CPFPessoa)) 
        {
            return dado[i].getPosAqrByte();
        }
        else 
        {
            return -1;
        }
    }

    public boolean delete(Bucket a, int b) 
    {
        boolean resposta = true;

        if (vazio(a) == true) 
        {
            for (int i = 0; i < a.quantDadosBucket; i++) 
            {
                if (a.dado[i].getCPFpessoa() == b) 
                {
                    a.dado[i].setCPFpessoa(-1);
                    resposta = true;
                }
            }
        } 
        else 
        {
            resposta = false;
        }
        return resposta;
    }

    public void imprimir(Bucket a) 
    {
       
        for (int i = 0; i < a.getQuantDadosBucket(); i++) 
        {
            System.out.println("ENTREI DENTRO DO BUCKET METODO IMPRIMIR");
            System.out.println("Bucket: ");
            System.out.print("-"+a.getProfLocal());
            a.dado[i].imprime();
            System.out.print("-]");
        }

    }

    public boolean vazio(Bucket a) 
    {
        boolean resposta = false;

        for (int i = 0; i < a.quantDadosBucket; i++) 
        {
            if (a.dado[i] == null) 
            {
                resposta = true;
            } 
            else 
            {
                resposta = false;
                break;
            }
        }
        return resposta;
    }

    public boolean cheio(Bucket a) 
    {
        boolean resposta = true;

        for (int i = 0; i < a.quantDadosBucket; i++) 
        {
            if (a.dado[i] != null) 
            {
                resposta = true;
            } 
            else 
            {
                resposta = false;
                break;
            }
        }
        return resposta;
    }

    public byte[] toByteArray() throws IOException 
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        
        for (int i = 0; i < quantDadosBucket; i++) 
        {
            dos.writeInt(profLocal);
            dos.writeLong(dado[i].getPosAqrByte());
            dos.writeInt(dado[i].getCPFpessoa());
            dos.flush();
        }

        return baos.toByteArray();
    }

    public void fromByteArray(byte[] ba) throws IOException 
    {
        ParPessoa a = new ParPessoa(0,0);
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);

        for (int i = 0; i < quantDadosBucket; i++) 
        {
            profLocal = dis.readInt();
            a.setCPFpessoa(dis.readInt());
            a.setPosAqrByte(dis.readLong());
            dado[i] = a;
        }
    }
}
