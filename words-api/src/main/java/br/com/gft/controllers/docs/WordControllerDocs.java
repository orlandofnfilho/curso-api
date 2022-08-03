package br.com.gft.controllers.docs;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import br.com.gft.dto.word.QueryWordDTO;
import br.com.gft.dto.word.RegisterWordDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api("Manages word")
public interface WordControllerDocs {
	

	public static final String WORD_WITH_GIVEN_ID_NOT_FOUND = "Word with given id not found.";
	public static final String SUCCESS_GAME_DELETED_IN_THE_SYSTEM = "Success game deleted in the system";
	public static final String DATA_INTEGRATY_VIOLATION_WORD_IS_ALREADY_REGISTRED = "Data Integraty Violation, word is already registred";
	public static final String WORD_WITH_GIVEN_NAME_NOT_FOUND = "Word with given name not found.";
	public static final String SUCCESS_WORD_UPDATE_IN_THE_SYSTEM = "Success word update in the system";
	public static final String PAGEABLE_LIST_OF_ALL_WORDS_REGISTERED_IN_THE_SYSTEM = "Pageable list of all words registered in the system";
	public static final String SUCCESS_WORD_FOUND_IN_THE_SYSTEM = "Success word found in the system";
	public static final String DATA_INTEGRITY_VIOLATION_WORD_IS_ALREADY_REGISTRED = "Data Integrity Violation, word is already registred";
	public static final String FORBIDDEN_DON_T_HAVE_PERMISSION_TO_ACCESS_THIS = "Forbidden. Don't have permission to access this";
	public static final String MISSING_REQUIRED_FIELDS_OR_WRONG_FIELD_RANGE_VALUE = "Missing required fields or wrong field range value.";
	public static final String SUCCESS_WORD_CREATION = "Success word creation";

	@ApiOperation(value = "Word creation operation")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = SUCCESS_WORD_CREATION),
            @ApiResponse(code = 400, message = MISSING_REQUIRED_FIELDS_OR_WRONG_FIELD_RANGE_VALUE),
            @ApiResponse(code = 403, message = FORBIDDEN_DON_T_HAVE_PERMISSION_TO_ACCESS_THIS),
            @ApiResponse(code = 409, message = DATA_INTEGRITY_VIOLATION_WORD_IS_ALREADY_REGISTRED)
    })
	ResponseEntity<QueryWordDTO> create(RegisterWordDTO obj);

	@ApiOperation(value = "Returns word found by a given id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = SUCCESS_WORD_FOUND_IN_THE_SYSTEM),
            @ApiResponse(code = 403, message = FORBIDDEN_DON_T_HAVE_PERMISSION_TO_ACCESS_THIS),
            @ApiResponse(code = 404, message = WORD_WITH_GIVEN_NAME_NOT_FOUND)
    })
	ResponseEntity<QueryWordDTO> findById(Long id);

	@ApiOperation(value = "Returns a pageable list of all words registered in the system")
    @ApiResponses(value = {
    		@ApiResponse(code = 200, message = PAGEABLE_LIST_OF_ALL_WORDS_REGISTERED_IN_THE_SYSTEM),
    		@ApiResponse(code = 403, message = FORBIDDEN_DON_T_HAVE_PERMISSION_TO_ACCESS_THIS),
    })
	public ResponseEntity<Page<QueryWordDTO>> findAll(Pageable pageable);

	@ApiOperation(value = "Update word by a given id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = SUCCESS_WORD_UPDATE_IN_THE_SYSTEM),
            @ApiResponse(code = 400, message = MISSING_REQUIRED_FIELDS_OR_WRONG_FIELD_RANGE_VALUE),
            @ApiResponse(code = 403, message = FORBIDDEN_DON_T_HAVE_PERMISSION_TO_ACCESS_THIS),
            @ApiResponse(code = 404, message = WORD_WITH_GIVEN_NAME_NOT_FOUND),
            @ApiResponse(code = 409, message = DATA_INTEGRATY_VIOLATION_WORD_IS_ALREADY_REGISTRED)
    })
	public ResponseEntity<QueryWordDTO> update(Long id, RegisterWordDTO obj);
	
	@ApiOperation(value = "Delete a word found by a given valid Id")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = SUCCESS_GAME_DELETED_IN_THE_SYSTEM),
            @ApiResponse(code = 403, message = FORBIDDEN_DON_T_HAVE_PERMISSION_TO_ACCESS_THIS),
            @ApiResponse(code = 404, message = WORD_WITH_GIVEN_ID_NOT_FOUND)
    })
	public ResponseEntity<QueryWordDTO> delete(Long id);
}
