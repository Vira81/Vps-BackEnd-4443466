
INSERT INTO public.vps_pessoa(data_nascimento, id, cpf, nome, telefone) VALUES
('1980-01-01', 1, '11111111111', 'Admin', '(11) 90000-0001'),
('1985-05-10', 2, '22222222222', 'João Médico', '(11) 90000-0002'),
('1990-08-15', 3, '33333333333', 'Maria Santos', '(11) 90000-0003'),
('1995-03-22', 4, '44444444444', 'Carlos Silva', '(11) 90000-0004'),
('1982-07-09', 5, '55555555555', 'Fernanda Medicca', '(11) 90000-0005'),
('2000-11-30', 6, '66666666666', 'Lucas Rocha', '(11) 90000-0006'),
('1978-04-05', 7, '77777777777', 'Beto Dias', '(11) 90000-0007'),
('1992-09-18', 8, '88888888888', 'André Medicacao', '(11) 90000-0008'),
('1998-12-25', 9, '99999999999', 'Juliana Alves', '(11) 90000-0009'),
('1987-06-14', 10, '00000000000', 'Admin 2', '(11) 90000-0010'),
('1987-06-15', 11, '00000000001', 'Minha unica função é ser apagado', '(11) 90000-0010');



INSERT INTO public.vps_usuario(data_criacao, id, pessoa_id, ultimo_acesso, email, perfil, senha_hash, status) VALUES
('2025-06-22', 1, 1, NULL, 'admin@vida.com', 'ADMIN', '$2a$10$hSq2T2xHpHR9rEKOBGJUkOOUx8Zut4QYCyRZRZYcGDsLkiJgPQ3pG', 'ATIVO'),
('2025-06-22', 2, 2, NULL, 'medico@vida.com', 'PROFISSIONAL', '$2a$10$YydblE.cc/0NoGfr4HRI8Om9hdhmf1qIjtE7drM8zSQvhhRV56dtu', 'ATIVO'),
('2025-06-22', 3, 3, NULL, 'paciente@vida.com', 'PACIENTE', '$2a$10$5uJ/E7v5hCkCqKaUi4siEeSz1K4Ck4ned9uLwnju10CDnPP5LvLVO', 'ATIVO'),
('2025-06-22', 4, 4, NULL, 'user4@vida.com', 'PACIENTE', '$2a$10$3C9lSfr6y8Z/AgY3zDp1cuj4Rz07MHj4hPVd0ae35L/GHzbZ77Zaa', 'ATIVO'),
('2025-06-22', 5, 5, NULL, 'user5@vida.com', 'PROFISSIONAL', '$2a$10$dxM0McmrcvfA9GSUftXUeOjP8RI/gYipIWRD0rddEr6ruECrI1sAC', 'ATIVO'),
('2025-06-22', 6, 6, NULL, 'user6@vida.com', 'PACIENTE', '$2a$10$rhD2wKV0PqmeZbh/XCXyC.NoyngshpupXoAqkFwqmwPX/170xfhbK', 'ATIVO'),
('2025-06-22', 7, 7, NULL, 'user7@vida.com', 'PACIENTE', '$2a$10$1w/Y4wWxMaWRfR7lhw9xZ.mDOiwTAVzzJS9HEeheMaAipTZ0FnyWy', 'ATIVO'),
('2025-06-22', 8, 8, NULL, 'user8@vida.com', 'PROFISSIONAL', '$2a$10$RAsEEeyg2dRuSAOg.8jF/uFkpYgZqO0sy1Y9RhUzyNTN6nALfYwcu', 'ATIVO'),
('2025-06-22', 9, 9, NULL, 'user9@vida.com', 'PACIENTE', '$2a$10$3LfUqtbnE9hv2vI9.6LXOe2AagFJtI9P97NKRLikzVyg4QZgfTvDS', 'ATIVO'),
('2025-06-22', 10, 10, NULL, 'user10@vida.com', 'ADMIN', '$2a$10$tv7CCNTUQ40gk/AlG0G30OzgwT6SjWQrP146HOJwKZeMM6.deXy/q', 'INATIVO'),
('2025-06-22', 11, 11, NULL, 'user11@vida.com', 'PACIENTE', '$2a$10$3LfUqtbnE9hv2vI9.6LXOe2AagFJtI9P97NKRLikzVyg4QZgfTvDS', 'PENDENTE');




INSERT INTO public.vps_hospital(id, endereco, nome, telefone) VALUES
(1, 'Rua Saúde Pereira, 123 - São Paulo, SP', 'Hospital Vida Plus', '(11) 3000-0001'),
(2, 'Av. Medica Central, 456 - Rio de Janeiro, RJ', 'Hospital Bem Estar', '(21) 4000-0002'),
(3, 'Rua Enfermagem, 789 - Belo Horizonte, MG', 'Clinica Esperança', '(31) 5000-0003'),
(4, NULL, 'Telemedicina', NULL);



INSERT INTO public.vps_profissionalsaude(
	data_criacao_profissional, id, pessoa_id, usuario_id, crm, especialidade, funcao) 
VALUES 
('2025-06-22', 1, 2, 2, '123456-SP', 'CARDIOLOGIA', 'MEDICA'),
('2025-06-22', 2, 5, 5, '123456-SP', 'PEDIATRIA', 'MEDICA'),
('2025-06-22', 3, 8, 8, '654321-SP', 'OUTRA', 'TECNICA');


INSERT INTO public.vps_dias_trabalho(profissional_id, dia_semana) VALUES
(1, 'MONDAY'),
(1, 'WEDNESDAY'),
(1, 'FRIDAY'),
(2, 'TUESDAY'),
(2, 'THURSDAY');




INSERT INTO public.vps_profissional_hospital(hospital_id, profissional_id) VALUES
(1, 1),
(2, 1),
(1, 2),
(3, 2),
(2, 3);










INSERT INTO public.vps_leito(data_liberacao, data_ocupacao, hospital_id, id, paciente_id, identificador, status) VALUES
(NULL, NULL, 1, 1, NULL, 'A101', 'LIVRE'),
(NULL, '2025-06-20 10:00:00', 1, 2, 3, 'A102', 'OCUPADO');


INSERT INTO public.vps_leito(data_liberacao, data_ocupacao, hospital_id, id, paciente_id, identificador, status) VALUES
(NULL, NULL, 2, 3, NULL, 'B201', 'LIVRE'),
(NULL, NULL, 2, 4, NULL, 'B202', 'LIVRE');


INSERT INTO public.vps_leito(data_liberacao, data_ocupacao, hospital_id, id, paciente_id, identificador, status) VALUES
(NULL, '2025-06-21 15:00:00', 3, 5, 6, 'C301', 'OCUPADO'),
(NULL, NULL, 3, 6, NULL, 'C302', 'MANUTENCAO');



INSERT INTO public.vps_consulta(
	dia, hora, valor, data_criacao_consulta, data_realizada, hospital_id, id, paciente_id, profissional_id, status_consulta, teleconsulta)
VALUES
('2025-06-15', '09:00:00', 200.00, '2025-06-10', NULL, 1, 1, 3, 1, 'AGENDADA', false),
('2025-07-05', '14:30:00', 250.00, '2025-06-22', NULL, 1, 2, 4, 1, 'AGENDADA', false),
('2025-06-10', '11:00:00', 180.00, '2025-06-05', NULL, 2, 3, 6, 2, 'AGENDADA', false),
('2025-06-25', '10:00:00', 220.00, '2025-06-20', NULL, 3, 4, 7, 2, 'CANCELADA_PACIENTE', false),
('2025-06-23', '08:30:00', 300.00, '2025-06-22', NULL, 3, 5, 3, 1, 'AGENDADA', false),
('1981-11-23', '08:30:00', 300.00, '2025-06-22', NULL, 1, 6, 3, 3, 'EXPIRADA', false),
('2025-07-12', '08:30:00', 300.00, '2025-06-22', NULL, 2, 7, 3, 1, 'CANCELADA_PROFISSIONAL', false),
('2025-07-13', '08:30:00', 300.00, '2025-06-22', NULL, 3, 8, 3, 3, 'AGENDADA', false),
('2025-06-27', '08:30:00', 300.00, '2025-06-22', NULL, 2, 9, 3, 1, 'AGENDADA', false);

-- Comando para descobrir o nome da sequencia
-- Ou ver no Schemas do Postgre 
--SELECT relname FROM pg_class WHERE relkind = 'S';


SELECT setval('vps_pessoa_seq', 11, true);

SELECT setval('vps_usuario_seq', 11, true);

SELECT setval('vps_profissionalsaude_id_seq', 3, true);

SELECT setval('vps_hospital_seq', 4, true);

SELECT setval('vps_leito_id_seq', 6, true);

SELECT setval('vps_consulta_seq', 9, true);