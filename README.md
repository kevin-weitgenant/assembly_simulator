## O que já foi feito

Getters e Setters pro registrador e pra memória.
Getters e Setters pra um bit(registrador de flag).  Para byte, **getbyte** ->argumentos->  **(nome_registrador, "MS") -> byte mais significativo   (nome_registrador, "LS") -> byte menos significativo**

Highlither funciona pelo número da linha dado
Tabela de variáveis é criada em um hashmap.   tabela_opds = new HashMap<String, List<Integer>>()    
                                              tabela_opds.put(op_nome, Arrays.asList(posicao,0))   ->  constante  
                                              tabela_opds.put(op_nome, Arrays.asList(posicao,1))   ->  variavel



## O que falta implementar
- Algumas operações, setar flags, segmento de dados e de instrução deve ser definido dinamicamente. 
- Tabela de símbolos deve também armazenar endereços de instruções pra fazer desvios.
- Pegar rótulos(colocar na tabela de simbolos)



## Coisas que foram implementadas mas devem ser modificadas
- Constantes não devem ir pra memória. Com constantes digo operandos declarados como **op1 EQU 30** ou algo como add AX,30.  Tanto 30 como op1 são constantes
- Segmentos de dados e codigo devem ser definidos dinamicamente. Consultar se pilha também(mandei e-mail pro professor)




# Trabalho de PS

## Time:

- Gerson Menezes
- Lourenço Mulling
- Kevin Castro Weitgenant
- Renã Souza
- Mathaus Huber
- Vitor Torino
- Wilians Junior


