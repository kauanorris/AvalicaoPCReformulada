# 🚗 Sistema de Produção e Comercialização de Veículos

Este projeto é uma simulação de uma cadeia logística automotiva distribuída, desenvolvida em **Java**. O sistema utiliza sockets TCP para comunicação entre instâncias e threads para gerenciar a produção concorrente.

## 🏗️ Arquitetura do Sistema

O sistema é dividido em três entidades principais que rodam de forma independente:

1.  **Fábrica (Servidor Central):**
    * Possui 4 estações de produção.
    * Cada estação tem 5 funcionários operando em uma estrutura circular (Problema dos Filósofos Comensais).
    * Gerencia um estoque de 500 peças e uma esteira de saída com capacidade para 40 veículos.
2.  **Lojas (Intermediários):**
    * Atuam como clientes da fábrica e servidores para os usuários finais.
    * Solicitam veículos à fábrica conforme a demanda e mantêm um estoque local.
3.  **Clientes (Consumidores):**
    * 20 threads simulando compradores aleatórios.
    * Escolhem lojas randômicas para realizar tentativas de compra.

---

## 🛠️ Tecnologias Utilizadas

* **Java SE** (Core)
* **Sockets TCP/IP** (Comunicação de rede)
* **Threads & Semaphores** (Concorrência)
* **BlockingQueues** (Gerenciamento de estoque/esteiras)
* **ReentrantLocks** (Sincronização de ferramentas)

---

## 🚀 Como Executar

Siga exatamente a ordem abaixo para garantir que os servidores estejam prontos para receber conexões.

### 1. Compilação
Abra o terminal na pasta raiz do projeto e compile todos os arquivos:
```bash
javac *.java
