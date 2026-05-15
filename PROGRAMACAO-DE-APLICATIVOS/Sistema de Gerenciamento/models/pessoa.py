class Pessoa:
    """Classe base que representa uma pessoa no sistema."""

    def __init__(self, nome: str, telefone: str, email: str):
        self.__nome = nome
        self.__telefone = telefone
        self.__email = email
        self.__id = None

    # --- Getters e Setters ---

    @property
    def id(self):
        return self.__id

    @id.setter
    def id(self, valor: int):
        if valor is not None and valor <= 0:
            raise ValueError("ID deve ser um número positivo.")
        self.__id = valor

    @property
    def nome(self):
        return self.__nome

    @nome.setter
    def nome(self, valor: str):
        if not valor or not valor.strip():
            raise ValueError("Nome não pode ser vazio.")
        self.__nome = valor.strip()

    @property
    def telefone(self):
        return self.__telefone

    @telefone.setter
    def telefone(self, valor: str):
        self.__telefone = valor

    @property
    def email(self):
        return self.__email

    @email.setter
    def email(self, valor: str):
        self.__email = valor

    def apresentar(self) -> str:
        """Método polimórfico — cada subclasse sobrescreve com sua apresentação."""
        return f"Pessoa: {self.__nome} | Tel: {self.__telefone}"

    def __repr__(self):
        return self.apresentar()
