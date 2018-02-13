package com.tcc.lojavirtual.resource;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.tcc.lojavirtual.domain.Cliente;
import com.tcc.lojavirtual.dto.ClienteDTO;
import com.tcc.lojavirtual.dto.ClienteNewDTO;
import com.tcc.lojavirtual.service.ClienteService;

@RestController
@RequestMapping(value="/clientes")
public class ClienteResource {

	@Autowired
	private ClienteService clienteService;
	
	
	@RequestMapping(value="/{id}",method=RequestMethod.GET)
	public ResponseEntity<Cliente> find(@PathVariable Integer id){
		Cliente obj = clienteService.find(id);
		return ResponseEntity.ok().body(obj);
	}
	
	
	
	@RequestMapping(method=RequestMethod.GET)
	public ResponseEntity<List<ClienteDTO>> findAll(){
		List<Cliente> list = clienteService.findAll();
		List<ClienteDTO> dto = list.stream().map(obj -> new ClienteDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok().body(dto);
	}
	
	
	
	@RequestMapping(value="/page",method=RequestMethod.GET)
	public ResponseEntity<Page<ClienteDTO>> findPage(
			@RequestParam(value="page", defaultValue="0") Integer page, 
			@RequestParam(value="linesPage", defaultValue="24")Integer linesPage, 
			@RequestParam(value="order", defaultValue="nome")String order, 
			@RequestParam(value="direction", defaultValue="ASC")String direction
			){
		
		Page<Cliente> list = clienteService.findPage(page, linesPage, order, direction);
		Page<ClienteDTO> listDto = list.map(obj -> new ClienteDTO(obj));
		return ResponseEntity.ok().body(listDto);
	}
	

	
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<Void> insert(@Valid @RequestBody ClienteNewDTO objDto){
		Cliente obj = clienteService.fromNewDTO(objDto);
		obj = clienteService.insert(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(obj.getCodigoCliente()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	

	@RequestMapping(value="/{id}",method=RequestMethod.PUT)
	public ResponseEntity<Void> update(@PathVariable Integer id, @Valid @RequestBody ClienteDTO objDto){
		Cliente obj = clienteService.fromDTO(objDto);
		obj.setCodigoCliente(id);
		obj = clienteService.update(obj);
		return ResponseEntity.noContent().build();
	}
	

	@RequestMapping(value="/{id}",method=RequestMethod.DELETE)
	public ResponseEntity<Void> delete(@PathVariable Integer id){
		clienteService.delete(id);
		return ResponseEntity.noContent().build();
	}
}