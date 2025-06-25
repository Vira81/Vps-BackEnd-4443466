import uuid

from locust import HttpUser, task, between
import random


class Usuario(HttpUser):
    wait_time = between(1, 3)

    def on_start(self):
        # Cadastrar usuário
        cpf = str(random.randint(10000000000, 99999999999))
        self.email = f"user{cpf}@test.com"
        self.senha = "123456"

        cadastro = {
            "email": self.email,
            "senha": self.senha,
        }

        dados_pess = {
            "nome": "Teste Locust",
            "cpf": cpf,
            "dataNascimento": "1995-01-01",
            "telefone": "(21)912345678"
        }

        self.client.post("/usuarios/cadastro", json=cadastro)


        # Login
        response = self.client.post("/auth/login", json={
            "username": self.email,
            "password": self.senha
        })

        self.token = "Bearer " + response.json()["token"]
        self.headers = {"Authorization": self.token}

        # Atualiza dados pessoais
        self.client.put("/dados_pessoais", json=dados_pess, headers=self.headers)

    @task
    def agendar_consulta(self):
        consulta = {
            "profissionalId": 1,
            "hospitalId": 1,
            "dia": "2025-06-25",
            "hora": "15:00"
        }

        # cria consultas
        self.client.post("/consulta/nova_consulta", json=consulta, headers=self.headers)

        # busca as consultas
        self.client.get("/consulta/minhas", headers=self.headers)
