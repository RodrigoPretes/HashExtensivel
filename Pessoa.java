import java.io.*;
public class Pessoa {
  private int lapide;
  private int CPF;
  private String nome;
  private String nascimento;
  private String sexo;
  private String anot;
  private int TAM_anot;
  //public static final int Pessoa_TAM = 23;

public Pessoa() {}
public Pessoa (int lapide, int CPF, String nome, String nascimento, String sexo, int TAM_anot,String anot){
  this.lapide = lapide;
  this.CPF = CPF;
  if (nome.length()>15){
      this.nome = nome.substring(0,14);
  }
  else{
      this.nome = nome;
  } 
  if (sexo.length()>2){
  this.sexo = sexo.substring(0, 1);
  }
  else{
      this.sexo = sexo;
  }
  if (nascimento.length()>12){
      this.nascimento = nascimento.substring(0, 11);
  }
  else{
      this.nascimento = nascimento;
  }
  if (anot.length()>TAM_anot){
    this.anot = anot.substring(0, TAM_anot-1);
  }
  else{
  this.anot = ("-");
    for (int i=0; i <TAM_anot-1; i++){
        this.anot += anot.concat("-");
    }
}
  
}

public String toString (){
  return
         "\nCPF................: " + this.CPF + 
         "\nNome...............: " + this.nome + 
         "\nData de Nascimento.: " + this.nascimento + 
         "\nSexo...............: "  + this.sexo +
         "\nAnotações do Médico: "  + this.anot;
}
public int getLapide() {return lapide;}
public int getCPF() {return CPF;}
public String getNome() {return nome;}
public String getNascimento() {return nascimento;}
public String getSexo() {return sexo;}
public int getTamAnot(){return TAM_anot;}
public String getAnot() {return anot;}
public void setLapide(int lapide){this.lapide = lapide;}
public void setCpf(int CPF) {this.CPF = CPF;}
public void setNome(String nome){this.nome = nome;}
public void setNascimento(String nascimento) {this.nascimento = nascimento;}
public void setSexo(String sexo) {this.sexo = sexo;}
public void setAnot(String anot)
    {if (anot.length()>TAM_anot+1){
    this.anot = anot.substring(0, TAM_anot);}}

public byte[] toByteArray() throws IOException 
{
  ByteArrayOutputStream baos = new ByteArrayOutputStream();
  DataOutputStream dos = new DataOutputStream(baos);
  
  dos.writeInt(lapide);
  dos.writeInt(CPF);
  dos.writeUTF(nome);
  dos.writeUTF(nascimento);
  dos.writeUTF(sexo);
  dos.writeUTF(anot);
  dos.flush();

  return baos.toByteArray();
}

public void fromByteArray(byte[] ba) throws IOException 
{
  ByteArrayInputStream bais = new ByteArrayInputStream(ba);
  DataInputStream dis = new DataInputStream(bais);

  lapide = dis.readInt();
  CPF = dis.readInt();
  nome = dis.readUTF();
  nascimento = dis.readUTF();
  sexo = dis.readUTF();
  anot = dis.readUTF();
}

/*public void write (RandomAccessFile file , long ponteiro)
{
  try
  {
    file.seek(ponteiro * Pessoa_TAMANHO);
    file.write(toByteArray());
  }
  catch (IOException e)
  {
    System.out.println("Não foi possivel realizar a escrita");
  }
}

public int read (RandomAccessFile file , long ponteiro) 
{
  int nbytes = 0;

  try
  {
    byte[] ba = new byte [Pessoa_TAMANHO];
    file.seek(ponteiro * Pessoa_TAMANHO);
    nbytes = file.read(ba);

    if (nbytes > 0)
      fromByteArray(ba);
  }
  catch(IOException e)
  {
    System.out.println("Não foi possivel realizar a leitura");
  }

  return nbytes;
}*/
}