import java.io.*;
import java.util.*;

public class Main 
{

  public static void main(String[] args) throws Exception  
  {
    InputStreamReader r = new InputStreamReader(System.in);
    BufferedReader br = new BufferedReader(r);
    Scanner entrada = new Scanner(System.in);
    TabelaHashExtensivel hash; 
    int n,lapide,cpf,tam_anot=0,tam_bucket=1;
    String nome,dataNascimento,sexo,anotMed;

    

    System.out.println("=================================================================");
    System.out.println("BEM VINDO AO SISTEMA DA EMPRESA DE PLANOS DE SAÚDE VIVER FAZ BEM!");
    System.out.println("=================================================================");
    System.out.println("Selecione uma opção do menu para continuar:\n");
    System.out.println("1 - Criar registro\n2 - Editar registro\n3 - Remover registro\n4 - Imprimir dados do arquivo\n");
    System.out.print(">");
    n = entrada.nextInt();
    
    RandomAccessFile arq;

    byte ba[];

    try 
    {
      arq = new RandomAccessFile("Estudos/ArquivoMestre.db","rw");
      Pessoa paciente = new Pessoa();
      if(arq.length()==0){ 
      System.out.println("Parametros Iniciais para primeira inicialização do sistema: ");
      System.out.println("Qual o tamanho do campo anotação para os pacientes?");
      tam_anot = entrada.nextInt();
      System.out.println("Qual o tamanho do bucket?");
      tam_bucket = entrada.nextInt();
      }
      hash = new TabelaHashExtensivel("Estudos/diretorio.db", "Estudos/cestos.db", tam_bucket);
      while(n!=0)
      {
        
        //INSERÇÃO NO ARQUIVO-MESTRE
        if (n == 1)
        {
          int flag = 1;

          while(flag!=0)
          { 
            
            lapide = 1;

            System.out.println("\nQual o CPF do paciente? ");
            cpf = entrada.nextInt();

            System.out.println("Qual o nome do paciente?");
            nome = br.readLine();

            System.out.println("Qual a data de nascimento do paciente?");
            dataNascimento = entrada.next();

            System.out.println("Qual o sexo do paciente?");
            sexo = entrada.next();
            

            anotMed = ("");
            
            paciente = new Pessoa(lapide, cpf, nome, dataNascimento, sexo, tam_anot,anotMed);
            ba = paciente.toByteArray();
            long pos=0;
            pos = arq.length();
            arq.seek(pos);
            ParPessoa a = new ParPessoa(paciente.getCPF(),pos);
            hash.create(a);
            arq.seek(pos);
            arq.writeInt(ba.length);
            arq.write(ba);
             
            System.out.println("Deseja continuar a inserção de registros?  1 - SIM 0 - NÃO");
            flag = entrada.nextInt();
          }
        }

        //ALTERAÇÃO DO ARQUIVO-MESTRE
        if (n == 2)
        {
          int flag = 1; 

          while(flag!=0)
          {
            int buscaCPF;
            int numBuscas = 0;
            String campoAnotacao;

            System.out.println("Digite o CPF da pessoa que você deseja fazer uma alteração:");
            buscaCPF = entrada.nextInt();

            long pos = 0;

            int encontrouCPF = 0;

            while(encontrouCPF!=1)
            { 
              Pessoa leitura = new Pessoa();
              int tamArquivo;
              pos = hash.read(buscaCPF);
              arq.seek(pos);
              tamArquivo = arq.readInt();
              ba = new byte[tamArquivo];
              arq.read(ba);
              leitura.fromByteArray(ba);

              numBuscas++;

              if(leitura.getCPF() == buscaCPF)
              {
                System.out.println("Paciente encontrado, digite a anotação:");
                campoAnotacao = br.readLine();

                leitura.setAnot(campoAnotacao);
                arq.seek(pos);
                ba = leitura.toByteArray();
                arq.writeInt(ba.length);
                arq.write(ba);

                encontrouCPF = 1;
              }

              if (pos==arq.length())
              {
                System.out.println("Não foi encontrado um paciente com esse numero de CPF");
                break;
              }
            }
 
            System.out.println("Nesse caso foram lidos registros -> " + numBuscas);
            System.out.println("Deseja continuar fazendo mais alguma anotação? 1 - SIM 0 - NÃO");
            flag = entrada.nextInt();
          } 
        }

        //EXCLUSÃO DO ARQUIVO-MESTRE
        if (n == 3)
        {
          int flag = 1; 

          while(flag!=0)
          {
            int buscaCPF;
            int numBuscas=0;

            System.out.println("Digite o CPF da pessoa que você deseja excluir do registro:");
            buscaCPF = entrada.nextInt();

            long pos = 0;

            int encontrouCPF = 0;

            while(encontrouCPF!=1)
            {
              Pessoa leitura = new Pessoa();
              int tamanhoArquivo;
              pos = hash.read(buscaCPF);
              arq.seek(pos);
              tamanhoArquivo=arq.readInt();
              ba = new byte[tamanhoArquivo];
              arq.read(ba);
              leitura.fromByteArray(ba);
              numBuscas++;

              if (leitura.getCPF() == buscaCPF)
              {
                leitura.setLapide(-1);
                arq.seek(pos);
                ba = leitura.toByteArray();
                arq.writeInt(ba.length);
                arq.write(ba);
                hash.delete(buscaCPF);
                encontrouCPF = 1;

                System.out.println("Paciente encontrado, removendo...");
              }

              if(leitura.getCPF() != buscaCPF)
              {
                pos = arq.getFilePointer();

                System.out.println("Realizando mais uma leitura");
              }

              if (pos==arq.length())
              {
                System.out.println("Não foi encontrado um paciente com esse numero de CPF");
                break;  
              }
           
              System.out.printf("Nesse caso foram lidos %d registros " , numBuscas);
            }

            System.out.println("Deseja excluir mais pacientes? 1 - SIM 0 - NÃO");
            flag = entrada.nextInt();  
          }
        }

        //IMPRESSÃO DO ARQUIVO-MESTRE
        if (n == 4)
        {
          long pos = 0;

          while(true)
          {
            Pessoa leitura = new Pessoa();
            int tamanhoArquivo;
            arq.seek(pos);
            tamanhoArquivo=arq.readInt();
            ba = new byte[tamanhoArquivo];
            arq.read(ba);
            leitura.fromByteArray(ba);  

            
            System.out.println(leitura);
            pos = arq.getFilePointer();
            if (pos==arq.length()){
              break;
            }
          }
          hash.imprime();
        }

        System.out.println("Deseja realizar mais alguma ação? ");
        System.out.println("Selecione uma opção:");
        System.out.println("1 - Criar registro\n2 - Editar registro\n3 - Remover registro\n4 - Imprimir dados do arquivo\n");
        System.out.println(">");
        n = entrada.nextInt();
      }

      arq.close();
    } 
    catch (IOException e) 
    {
      e.printStackTrace();
    }
    

  } 
}
