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
import java.util.stream.Collectors;

@Component
public class ClienteService {

    @Autowired
    ClienteRepository clienteRepository;
    @Autowired
    EnderecoRepository enderecoRepository;

    @Transactional
    public ClienteDTO cadastrarCliente(ClienteDTO model) {
        Cliente cliente = new Cliente();
        Endereco endereco = new Endereco();

        cliente.setCpf(model.getCpf());
        cliente.setData_nascimento(model.getData_nascimento());
        cliente.setNome(model.getNome());
        cliente.setEmail(model.getEmail());
        cliente.setTelefone(model.getTelefone());

        clienteRepository.save(cliente);
        enderecoRepository.saveAll(model.getEnderecos());

        return model;
    }

    @Transactional
    public List<ClienteDTO> todosClientes() {
        List<ClienteDTO> model = new ArrayList<>();
        List<Cliente> clientes = clienteRepository.findAll();
        List<Endereco> todosEnderecos = enderecoRepository.findAll();

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
    }

    @Transactional
    public void atualizaCliente(ClienteDTO model) throws Exception {
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
                var enderecoAnterior = enderecoRepository.findById(endereco.getId());

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
            });

        } catch (Exception e) {
            throw new Exception("Não encontrado", e);
        }
    }

    @Transactional
    public ClienteDTO clienteByCPF(String cpf_cliente) {
        var clienteBanco = clienteRepository.findById(cpf_cliente);
        ClienteDTO clienteDTO = new ClienteDTO();

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
    }

    @Transactional
    public void deletaCliente(String cpf_cliente) {
        clienteRepository.deleteById(cpf_cliente);
        enderecoRepository.deleteAllByCpfCliente(cpf_cliente);
    }

    public void deletaEndereco(Long id) {
        enderecoRepository.deleteById(id);
    }

    @Transactional
    public void atualizaEndereco(Endereco endereco) throws Exception {
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
        } catch (Exception e) {
            throw new Exception("Não encontrado", e);
        }
    }

    @Transactional
    public void atualizaEnderecoPrincipal(Long id) throws Exception {

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
        } catch (Exception e) {
            throw new Exception("Não encontrado", e);
        }
    }
}
