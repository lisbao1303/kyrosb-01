package com.kyros.CadastroAPI.Service;

import com.kyros.CadastroAPI.DTO.ClienteDTO;
import com.kyros.CadastroAPI.Models.Cliente;
import com.kyros.CadastroAPI.Models.Endereco;
import com.kyros.CadastroAPI.Repository.ClienteRepository;
import com.kyros.CadastroAPI.Repository.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ClienteService {

    @Autowired
    ClienteRepository clienteRepository;
    @Autowired
    EnderecoRepository enderecoRepository;

    @Transactional
    public ClienteDTO cadastrarCliente(ClienteDTO model) throws Exception {
        Cliente cliente = new Cliente();

        try {
            cliente.setCpf(model.getCpf());
            cliente.setData_nascimento(model.getData_nascimento());
            cliente.setNome(model.getNome());
            cliente.setEmail(model.getEmail());
            cliente.setTelefone(model.getTelefone());

            clienteRepository.save(cliente);
            enderecoRepository.saveAll(model.getEnderecos());

            return model;

        } catch (Exception e) {
            throw new Exception("Não foi possivel realizar o cadastro", e);
        }

    }

    @Transactional
    public List<ClienteDTO> todosClientes() throws Exception {
        List<ClienteDTO> model = new ArrayList<>();
        List<Cliente> clientes = clienteRepository.findAll();
        List<Endereco> todosEnderecos = enderecoRepository.findAll();
        try {
            clientes.forEach(cliente -> {
                ClienteDTO clienteDTO = new ClienteDTO();
                clienteDTO.setCpf(cliente.getCpf());
                clienteDTO.setData_nascimento(cliente.getData_nascimento());
                clienteDTO.setNome(cliente.getNome());
                clienteDTO.setEmail(cliente.getEmail());
                clienteDTO.setTelefone(cliente.getTelefone());
                List<Endereco> enderecos = todosEnderecos.stream().filter(p -> p.getCpf_cliente().equals(cliente.getCpf())).collect(Collectors.toList());
                clienteDTO.setEnderecos(enderecos);
                model.add(clienteDTO);
            });
            return model;
        } catch (Exception e) {
            throw new Exception("Não encontrado", e);
        }
    }

    @Transactional
    public boolean atualizaCliente(ClienteDTO model) throws Exception {
        var clienteAnterior = clienteRepository.findById(model.getCpf());

        try {
            //Atualiza dados cadastrais Cliente
            if (clienteAnterior.isPresent()) {
                var clienteNovo = clienteAnterior.get();

                clienteNovo.setCpf(model.getCpf());
                clienteNovo.setData_nascimento(model.getData_nascimento());
                clienteNovo.setNome(model.getNome());
                clienteNovo.setEmail(model.getEmail());
                clienteNovo.setTelefone(model.getTelefone());

                clienteRepository.save(clienteNovo);
            }

            //Atualiza Endereços do Cliente
            model.getEnderecos().forEach(endereco -> {
                var enderecoAnterior = new Endereco();
                if(endereco.getId()!=null) enderecoAnterior = enderecoRepository.findById(endereco.getId()).get();

                if (enderecoAnterior.getId()!=null) {
                    var enderecoNovo = enderecoAnterior;

                    enderecoNovo.setCep(endereco.getCep());
                    enderecoNovo.setLogradouro(endereco.getLogradouro());
                    enderecoNovo.setNumero(endereco.getNumero());
                    enderecoNovo.setBairro(endereco.getBairro());
                    enderecoNovo.setCidade(endereco.getCidade());
                    enderecoNovo.setComplemento(endereco.getComplemento());
                    enderecoNovo.setUf(endereco.getUf());
                    enderecoNovo.setCpf_cliente(endereco.getCpf_cliente());
                    enderecoNovo.setIs_primario(endereco.getIs_primario());

                    enderecoRepository.save(enderecoNovo);
                }else{
                    enderecoRepository.save(endereco);
                }
            });

            return true;
        } catch (Exception e) {
            throw new Exception("Não encontrado", e);
        }
    }

    @Transactional
    public ClienteDTO clienteByCPF(String cpf_cliente) throws Exception {
        ClienteDTO clienteDTO = new ClienteDTO();

        try {
            var clienteBanco = clienteRepository.findById(cpf_cliente);
            if (clienteBanco.isPresent()) {
                var cliente = clienteBanco.get();

                clienteDTO.setCpf(cliente.getCpf());
                clienteDTO.setData_nascimento(cliente.getData_nascimento());
                clienteDTO.setNome(cliente.getNome());
                clienteDTO.setEmail(cliente.getEmail());
                clienteDTO.setTelefone(cliente.getTelefone());
                clienteDTO.setEnderecos(enderecoRepository.findByCpfClienteContains(cpf_cliente));
            }

            return clienteDTO;
        } catch (Exception e) {
            throw new Exception("Não encontrado", e);
        }

    }

    @Transactional
    public boolean deletaCliente(String cpf_cliente) throws Exception {
        try{
            clienteRepository.deleteById(cpf_cliente);
            enderecoRepository.findByCpfClienteContains(cpf_cliente).forEach(
                    endereco -> {
                        enderecoRepository.deleteById(endereco.getId());
                    }
            );
            return true;
        } catch (Exception e) {
            throw new Exception("Não encontrado", e);
        }
    }

    public boolean deletaEndereco(Long id) throws Exception {

        try{
            enderecoRepository.deleteById(id);
            return true;
        }catch (Exception e) {
            throw new Exception("Não encontrado", e);
        }
    }

    @Transactional
    public boolean atualizaEndereco(Endereco endereco) throws Exception {
        var enderecoAnterior = enderecoRepository.findById(endereco.getId());

        try {
            if (enderecoAnterior.isPresent()) {
                var enderecoNovo = enderecoAnterior.get();

                enderecoNovo.setCep(endereco.getCep());
                enderecoNovo.setLogradouro(endereco.getLogradouro());
                enderecoNovo.setNumero(endereco.getNumero());
                enderecoNovo.setBairro(endereco.getBairro());
                enderecoNovo.setCidade(endereco.getCidade());
                enderecoNovo.setComplemento(endereco.getComplemento());
                enderecoNovo.setUf(endereco.getUf());
                enderecoNovo.setCpf_cliente(endereco.getCpf_cliente());
                enderecoNovo.setIs_primario(endereco.getIs_primario());

                enderecoRepository.save(enderecoNovo);
            }
            return true;
        } catch (Exception e) {
            throw new Exception("Não encontrado", e);
        }
    }

    @Transactional
    public boolean atualizaEnderecoPrincipal(Long id) throws Exception {
        try {
            var novoPrincipalBD = enderecoRepository.findById(id);
            if (novoPrincipalBD.isPresent()) {
                var novoPrincipal = novoPrincipalBD.get();
                var antigoPrincipalBD = enderecoRepository.enderecoPrincipal(novoPrincipal.getCpf_cliente());
                antigoPrincipalBD.setIs_primario("N");
                enderecoRepository.save(antigoPrincipalBD);
                novoPrincipal.setIs_primario("S");
                enderecoRepository.save(novoPrincipal);
            }
            return true;
        } catch (Exception e) {
            throw new Exception("Não encontrado", e);
        }
    }
}
