# Portfólio Gabriel | Engenheiro de Dados & IA

Portfólio interativo de alta performance integrado com **Backend em Java**, **Banco de Dados Relacional H2 Persistente**, **Upload Seguro de Foto de Perfil** e canais de contato direto (**LinkedIn** e **WhatsApp**).

---

## 🚀 Novas Funcionalidades Implementadas

### 1. Upload Interativo de Foto de Perfil no Frontend
- **Interface Intuitiva**: Ao passar o cursor sobre a foto de perfil, é exibido o overlay com efeito glassmorphism e ícone de câmera *"Alterar Foto"*. Há também um botão flutuante estilizado para dispositivos móveis.
- **Drag & Drop**: Arraste e solte arquivos de imagem diretamente em cima do círculo de foto.
- **Validação no Cliente**: Aceita apenas `PNG`, `JPEG` e `WEBP` com tamanho máximo de 5 MB.
- **Preview Otimista**: Exibição imediata da imagem enquanto o upload é transmitido.
- **Notificações Toast**: Alertas dinâmicos no topo da tela informando o status do upload, sucesso ou eventuais erros.
- **Botão de Remoção/Restauração**: Permite remover a foto personalizada e restaurar o avatar original.

### 2. Backend Completo em Java (Clean Architecture & DTOs)
- **Linguagem & Framework**: Java 25 com Spring Boot 3.4 e Spring Data JPA.
- **Padrão DTO Rigoroso**:
  - `ApiResponseDTO<T>`: Envelope padronizado de resposta da API.
  - `ProfileImageResponseDTO`: Metadados da imagem salva, URL de download e timestamp.
  - `ImageMetadataDTO`: Detalhes e status da foto ativa.
  - `ContactMessageRequestDTO`: DTO validado com Bean Validation (`@NotBlank`, `@Size`).
  - `ContactMessageResponseDTO`: Confirmação do registro gravado no banco.
  - `ErrorResponseDTO`: Respostas de erro ricas e padronizadas.
- **Segurança Reforçada**:
  - **Inspeção de Magic Bytes**: Validação real dos primeiros bytes do arquivo (assinatura binária) para impedir o envio de scripts ou executáveis mascarados com extensão `.jpg`/`.png`.
  - **MIME Type Whitelist**: Somente formatos de imagem autorizados.
  - **Sanitização de Nomes de Arquivo**: Prevenção ativa contra ataques de Path Traversal (`../`).
  - **Sanitização de HTML**: Proteção contra Cross-Site Scripting (XSS) no formulário.
  - **Controle de CORS**: Configurado para aceitar requisições de navegadores locais e origens cruzadas.

### 3. Banco de Dados Persistente (H2 Database)
- **Persistência em Disco**: O banco é salvo no arquivo `./backend/data/portfolio_db.mv.db`, garantindo que uploads e mensagens não sejam perdidos ao reiniciar a máquina.
- **Tabelas Criadas**:
  - `profile_images`: Armazena a imagem em formato `BLOB`, metadados (nome, tamanho, tipo MIME), data de upload e flag de ativação.
  - `contact_messages`: Armazena todas as mensagens enviadas por recrutadores e empresas.
- **H2 Console Web**: Acesso administrativo pelo navegador em `http://localhost:8080/h2-console`.

### 4. Canais de Contato Direto (LinkedIn & WhatsApp)
- **Substituição do E-mail Nativo**: Em vez de abrir o cliente de e-mail do sistema (`mailto:`), agora o portfólio conecta o visitante diretamente ao:
  - **LinkedIn**: Botão para conexão profissional direta (`target="_blank"`).
  - **WhatsApp**: Link direto formatado (`https://wa.me/...`) para iniciar conversas imediatamente.
  - **GitHub**: Link direto para os repositórios.
- **Formulário para Recrutadores**:
  - Salva a proposta diretamente no banco de dados via API Java (`POST /api/contact`).
  - Oferece o botão *"Continuar Conversa no WhatsApp Agora"*, preenchendo automaticamente a mensagem do recrutador no WhatsApp.

---

## 🛠️ Como Executar

### Opção 1: Executar via Script Rápido (Recomendado)
Basta dar duplo clique no arquivo:
```cmd
start-backend.bat
```

### Opção 2: Executar via Linha de Comando
```powershell
cd backend
java -jar target/portfolio-backend-1.0.0.jar
```

O servidor iniciará na porta **8080**.

### Acessando a Aplicação:
- **Portfólio Completo**: Acesse [`http://localhost:8080`](http://localhost:8080) ou abra diretamente o arquivo `index.html` no seu navegador.
- **H2 Console**: [`http://localhost:8080/h2-console`](http://localhost:8080/h2-console)
  - JDBC URL: `jdbc:h2:file:./data/portfolio_db`
  - Usuário: `sa`
  - Senha: *(vazio)*

---

## 📡 Endpoints da API REST

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/api/profile/image` | Upload seguro da foto de perfil (Multipart/Form-Data) |
| `GET` | `/api/profile/image` | Retorna o binário da foto ativa (imagem direta) |
| `GET` | `/api/profile/image/metadata` | Retorna os metadados da foto em DTO |
| `DELETE` | `/api/profile/image` | Remove a foto customizada do banco |
| `POST` | `/api/contact` | Salva mensagem de recrutador no banco de dados |
| `GET` | `/api/contact` | Lista mensagens salvas no banco |
| `GET` | `/api/health` | Status de saúde da aplicação e do banco |
