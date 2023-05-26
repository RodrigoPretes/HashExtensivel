import java.io.*;

public class TabelaHashExtensivel 
{
    RandomAccessFile ArquivoBucket;
    RandomAccessFile ArquivoDiretorio;
    Bucket newBucket;
    Diretorio newDiretorio;
    int quantDadosBucket;

    public TabelaHashExtensivel(String nomeArquivoBucket, String nomeArquivoDiretorio, int quantN) throws IOException 
    {
        quantDadosBucket = quantN;
        ArquivoBucket = new RandomAccessFile(nomeArquivoBucket, "rw");
        ArquivoDiretorio = new RandomAccessFile(nomeArquivoDiretorio, "rw");

        if (ArquivoBucket.length() == 0 || ArquivoDiretorio.length() == 0) 
        {
            ParPessoa newPessoa = new ParPessoa();
            newDiretorio = new Diretorio();

            newDiretorio.setProfGlobal(0);
            newDiretorio.preencheDiretorio(0, 0);
            byte ba[] = newDiretorio.toByteArray();
            ArquivoDiretorio.seek(0);
            ArquivoDiretorio.write(ba);

            newBucket = new Bucket();
            ba = newBucket.toByteArray();
            ArquivoDiretorio.seek(0);
            ArquivoBucket.write(ba);

        }
    }

    public boolean create(ParPessoa a) throws Exception 
    {

        // recupera o tamanho do diretorio 
        byte[] vetByteD = new byte[(int) ArquivoDiretorio.length()];
     
        ArquivoDiretorio.seek(0);
        ArquivoDiretorio.read(vetByteD);
        newDiretorio.fromByteArray(vetByteD);

        // realiza o calculo da função hash e descobre em qual posicao do diretorio se encontra o bucket necessario para a criacao
        int i = newDiretorio.TabelaHash(newDiretorio, a.getCPFpessoa());

        // recupera o bucket do arquivo de bytes
        long posBucket = newDiretorio.getVetDiretorio(i);
        //vet para inicializacao ParPessoa
        ParPessoa[] vetParPessoa = new ParPessoa[quantDadosBucket];
        newBucket = new Bucket(1, quantDadosBucket, vetParPessoa);

        byte[] vetByteB = new byte[(int) ArquivoBucket.length()];
        ArquivoBucket.seek(posBucket);
        ArquivoBucket.read(vetByteB);
        newBucket.fromByteArray(vetByteB);

        // testa se a chave ja foi inserida
        if (newBucket.read(a.CPFpessoa, newBucket) != -1) 
        {
            throw new Exception("A pessoa com o CPF informado já foi previamente inserida");
        }

        // realiza o teste se o bucket está cheio, se não estiver realiza a inserção do ParPessoa
        if (newBucket.cheio(newBucket)) 
        {
            // insere a chave no cesto e o atualiza
            newBucket.update(newBucket, a);
            ArquivoBucket.seek(posBucket);
            ArquivoBucket.write(newBucket.toByteArray());
            return true;
        }

        // quando é necessario duplicar o diretorio
        int profundidadeLocalBucket = newBucket.getProfLocal();

        if (profundidadeLocalBucket >= newDiretorio.getProfGlobal()) 
        {
            newDiretorio.duplicaDiretorio(newDiretorio.getProfGlobal() + 1, newDiretorio);
        }

        int profundidadeGlobalDiretorio = newDiretorio.getProfGlobal();

        // criacao dos novos buckets
        Bucket newBucket1 = new Bucket(profundidadeLocalBucket + 1, quantDadosBucket, null);
        ArquivoBucket.seek(posBucket);
        ArquivoBucket.write(newBucket1.toByteArray());

        Bucket newBucket2 = new Bucket(profundidadeLocalBucket + 1, quantDadosBucket, null);
        long novaPosBucket = ArquivoBucket.length();
        ArquivoBucket.seek(novaPosBucket);
        ArquivoBucket.write(newBucket2.toByteArray());

        // atualização dos dados que ja estavam no diretorio
        int inicioArquivoDiretorio = newDiretorio.newTabelaHash(a.getCPFpessoa(), newBucket.getProfLocal());
        int desloca = (int) Math.pow(2, profundidadeLocalBucket);
        int tamanhoDiretorio = (int) Math.pow(2, profundidadeGlobalDiretorio);
        boolean atualiza = false;

        for (int j = inicioArquivoDiretorio; j < tamanhoDiretorio; j += desloca) 
        {
            if (atualiza) 
            {
                newDiretorio.setVetDiretorio(j, novaPosBucket);
            }
            atualiza = !atualiza;
        }

        // atualiza o arquivo Diretorio
        vetByteD = newDiretorio.toByteArray();
        ArquivoDiretorio.seek(0);
        ArquivoDiretorio.write(vetByteD);

        // reinserção dos valores no bucket
        for (int j = 0; j < newBucket.getQuantDadosBucket(); j++) 
        {
            create(a);
        }
        create(a);
        return false;
    }

    public long read(int CPFPessoa) throws Exception 
    {
        // recupera o tamanho do diretorio 
        byte[] vetByteD = new byte[(int) ArquivoDiretorio.length()];

        ArquivoDiretorio.seek(0);
        ArquivoDiretorio.read(vetByteD);
        newDiretorio.fromByteArray(vetByteD);

        // calculo da função hash 
        int i = newDiretorio.newTabelaHash(2, CPFPessoa);

        // recupera o bucket do arquivo de bytes
        long posBucket = newDiretorio.getVetDiretorio(i);
        newBucket = new Bucket(0, quantDadosBucket, null);

        byte[] vetByteB = new byte[(int) ArquivoBucket.length()];
        ArquivoBucket.seek(posBucket);
        ArquivoBucket.read(vetByteB);
        newBucket.fromByteArray(vetByteB);

        return newBucket.read(CPFPessoa, newBucket);
    }

    public boolean update(ParPessoa a) throws Exception 
    {
        // recupera o tamanho do diretorio 
        byte[] vetByteD = new byte[(int) ArquivoDiretorio.length()];

        ArquivoDiretorio.seek(0);
        ArquivoDiretorio.read(vetByteD);
        newDiretorio.fromByteArray(vetByteD);

        // funcao hash
        int i = newDiretorio.TabelaHash(newDiretorio, a.getCPFpessoa());

        // recupera o bucket do arquivo de bytes
        long posBucket = newDiretorio.getVetDiretorio(i);
        newBucket = new Bucket(0, quantDadosBucket, null);

        byte[] vetByteB = new byte[(int) ArquivoBucket.length()];
        ArquivoBucket.seek(posBucket);
        ArquivoBucket.read(vetByteB);
        newDiretorio.fromByteArray(vetByteB);

        // atualiza o dado
        if (!newBucket.update(newBucket, a)) 
        {
            return false;
        }

        // atualiza o bucket
        ArquivoBucket.seek(posBucket);
        ArquivoBucket.write(newBucket.toByteArray());

        return true;
    }

    public boolean delete(int CPFPessoa) throws IOException 
    {
        // recupera o tamanho do diretorio
        byte[] vetByteD = new byte[(int) ArquivoDiretorio.length()];

        ArquivoDiretorio.seek(0);
        ArquivoDiretorio.read(vetByteD);
        newDiretorio.fromByteArray(vetByteD);

        // funcao hash
        int i = newDiretorio.newTabelaHash(2, CPFPessoa);

        // recupera o bucket do arquivo de bytes
        long posBucket = newDiretorio.getVetDiretorio(i);
        newBucket = new Bucket(0, quantDadosBucket, null);

        byte[] vetByteB = new byte[(int) ArquivoBucket.length()];
        ArquivoBucket.seek(posBucket);
        ArquivoBucket.read(vetByteB);
        newDiretorio.fromByteArray(vetByteB);

        // exclusao do CPF 
        if (!newBucket.delete(newBucket, CPFPessoa)) 
        {
            return false;
        }

        // atualiza o bucket
        ArquivoBucket.seek(posBucket);
        ArquivoBucket.write(newBucket.toByteArray());

        return true;
    }

    public void imprime() 
    {
        try 
        {
            byte[] vetByteD = new byte[(int) ArquivoDiretorio.length()];
            ArquivoDiretorio.seek(0);
            ArquivoDiretorio.read(vetByteD);
            newDiretorio.fromByteArray(vetByteD);
            newDiretorio.imprimeDiretorio();

            System.out.println();
            System.out.println("Buckets: ");
            newBucket.imprimir(newBucket);
            ArquivoBucket.seek(0);


            /*while (ArquivoBucket.getFilePointer() != ArquivoBucket.length()) 
            {
                newBucket = new Bucket(0, quantDadosBucket, null);
                byte[] vetByteB = new byte[(int) ArquivoBucket.length()];
                ArquivoBucket.read(vetByteB);
                newBucket.fromByteArray(vetByteB);
                newBucket.imprimir(newBucket);
            }*/
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

}
