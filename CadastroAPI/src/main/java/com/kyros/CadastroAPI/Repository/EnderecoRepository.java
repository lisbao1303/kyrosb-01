package com.kyros.CadastroAPI.Repository;

import com.kyros.CadastroAPI.Models.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Long> {

    @Query(value = "SELECT * FROM endereco WHERE cpf_cliente = ?1", nativeQuery = true)
    List<Endereco> findByCpfClienteContains(String cpf_cliente);

    @Query(value = "DELETE FROM endereco WHERE cpf_cliente = ?1", nativeQuery = true)
    void deleteAllByCpfCliente(String cpf_cliente);

    @Query(value = "SELECT * FROM endereco WHERE cpf_cliente = ?1 AND is_primario = 'S' ", nativeQuery = true)
    Endereco enderecoPrincipal(String cpf_cliente);
}
