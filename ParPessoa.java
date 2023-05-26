public class ParPessoa 
{
    int CPFpessoa;
    long PosAqrByte;

    public ParPessoa(int CPFPessoa, long PosAqrByte){
        this.CPFpessoa = CPFPessoa;
        this.PosAqrByte = PosAqrByte;
    }
    public ParPessoa(){
    }
    public int getCPFpessoa() 
    {
        return CPFpessoa;
    }

    public void setCPFpessoa(int CPFpessoa) 
    {
        this.CPFpessoa = CPFpessoa;
    }

    public long getPosAqrByte() 
    {
        return PosAqrByte;
    }

    public void setPosAqrByte(long posAqrByte) 
    {
        this.PosAqrByte = posAqrByte;
    }

    public void imprime() 
    {
        System.out.print("-");
        System.out.print(getCPFpessoa() + "posMemoria:" + getPosAqrByte());
        System.out.println();
    }
}
