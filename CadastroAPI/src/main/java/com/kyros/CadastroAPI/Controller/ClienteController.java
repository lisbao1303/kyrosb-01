package com.kyros.CadastroAPI.Controller;

import com.kyros.CadastroAPI.DTO.ClienteDTO;
import com.kyros.CadastroAPI.Models.Cliente;
import com.kyros.CadastroAPI.Models.Endereco;
import com.kyros.CadastroAPI.Service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    ClienteService service;

    @PostMapping("/novo")
    public ClienteDTO cadastrarCliente(@RequestBody ClienteDTO model) throws Exception {
        return service.cadastrarCliente(model);
    }

    @CrossOrigin
    @GetMapping("/obtertodos")
    public List<ClienteDTO> todosClientes() throws Exception {
        return service.todosClientes();
    }

    @PutMapping("/atualiza")
    public void atualizaCliente(@RequestBody ClienteDTO model) throws Exception {
         service.atualizaCliente(model);
    }

    @GetMapping("/{cpf}")
    public ClienteDTO clienteByCPF(@PathVariable("cpf") String cpf_cliente) throws Exception {
        return service.clienteByCPF(cpf_cliente);
    }

    @DeleteMapping("/{cpf}")
    public void deletaCliente(@PathVariable("cpf") String cpf) throws Exception {
         service.deletaCliente(cpf);
    }


    @DeleteMapping("/endereco/{id}")
    public void deletaEndereco(@PathVariable("id") Long id) {
        service.deletaEndereco(id);
    }

    @PutMapping("/endereco/atualiza")
    public void atualizaEndereco(@RequestBody Endereco endereco) throws Exception {
        service.atualizaEndereco(endereco);
    }

    @PostMapping("/endereco/principal/{id}")
    public void atualizaEnderecoPrincipal(@PathVariable("id") Long id) throws Exception {
        service.atualizaEnderecoPrincipal(id);
    }

}
