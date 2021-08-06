package com.kyros.CadastroAPI.Controller;
import com.kyros.CadastroAPI.DTO.ClienteDTO;
import com.kyros.CadastroAPI.Models.Endereco;
import com.kyros.CadastroAPI.Service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public boolean atualizaCliente(@RequestBody ClienteDTO model) throws Exception {
        return service.atualizaCliente(model);
    }

    @GetMapping("/{cpf}")
    public ClienteDTO clienteByCPF(@PathVariable("cpf") String cpf_cliente) throws Exception {
        return service.clienteByCPF(cpf_cliente);
    }

    @DeleteMapping("/{cpf}")
    public boolean deletaCliente(@PathVariable("cpf") String cpf) throws Exception {
        return service.deletaCliente(cpf);
    }


    @DeleteMapping("/endereco/{id}")
    public boolean deletaEndereco(@PathVariable("id") Long id) throws Exception {
        return service.deletaEndereco(id);
    }

    @PutMapping("/endereco/atualiza")
    public boolean atualizaEndereco(@RequestBody Endereco endereco) throws Exception {
        return service.atualizaEndereco(endereco);
    }

    @PostMapping("/endereco/principal/{id}")
    public boolean atualizaEnderecoPrincipal(@PathVariable("id") Long id) throws Exception {
        return service.atualizaEnderecoPrincipal(id);
    }

}
