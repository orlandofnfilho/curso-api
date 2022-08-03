package br.com.gft.controllers.docs;

import org.springframework.http.ResponseEntity;

import br.com.gft.dto.TokenDTO;
import br.com.gft.entities.AuthenticationForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api("Create Token")
public interface AuthenticationControllerDocs {

	@ApiOperation(value = "Token creation operation")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success token creation"),
            @ApiResponse(code = 401, message = "User (email) or password invalid")
    })
	ResponseEntity<TokenDTO> authenticate(AuthenticationForm authForm);

}
