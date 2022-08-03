package br.com.gft.controllers.docs;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import br.com.gft.dto.TagDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api("Manages tag")
public interface TagControllerDocs {
	

	public static final String SUCCESS_TAG_UPDATE_IN_THE_SYSTEM = "Success tag update in the system";
	public static final String SUCCESS_TAG_DELETED_IN_THE_SYSTEM = "Success tag deleted in the system";
	public static final String PAGEABLE_LIST_OF_ALL_TAGS_REGISTERED_IN_THE_SYSTEM = "Pageable list of all tags registered in the system";
	public static final String TAG_WITH_GIVEN_NAME_NOT_FOUND = "Tag with given name not found.";
	public static final String SUCCESS_TAG_FOUND_IN_THE_SYSTEM = "Success tag found in the system";
	public static final String SUCCESS_TAG_CREATION = "Success tag creation";
	public static final String DATA_INTEGRITY_VIOLATION_TAG_IS_ALREADY_REGISTRED = "Data Integrity Violation, tag is already registred";
	public static final String MISSING_REQUIRED_FIELDS_OR_WRONG_FIELD_RANGE_VALUE = "Missing required fields or wrong field range value.";
	public static final String FORBIDDEN_DON_T_HAVE_PERMISSION_TO_ACCESS_THIS = "Forbidden. Don't have permission to access this";

	@ApiOperation(value = "Tag creation operation")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = SUCCESS_TAG_CREATION),
            @ApiResponse(code = 400, message = MISSING_REQUIRED_FIELDS_OR_WRONG_FIELD_RANGE_VALUE),
            @ApiResponse(code = 403, message = FORBIDDEN_DON_T_HAVE_PERMISSION_TO_ACCESS_THIS),
            @ApiResponse(code = 409, message = DATA_INTEGRITY_VIOLATION_TAG_IS_ALREADY_REGISTRED)
    })
	ResponseEntity<TagDTO> create(TagDTO obj);

	@ApiOperation(value = "Returns tag found by a given id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = SUCCESS_TAG_FOUND_IN_THE_SYSTEM),
            @ApiResponse(code = 403, message = FORBIDDEN_DON_T_HAVE_PERMISSION_TO_ACCESS_THIS),
            @ApiResponse(code = 404, message = TAG_WITH_GIVEN_NAME_NOT_FOUND)
    })
	ResponseEntity<TagDTO> findById(Long id);

	@ApiOperation(value = "Returns a pageable list of all tags registered in the system")
    @ApiResponses(value = {
    		@ApiResponse(code = 200, message = PAGEABLE_LIST_OF_ALL_TAGS_REGISTERED_IN_THE_SYSTEM),
    		@ApiResponse(code = 403, message = FORBIDDEN_DON_T_HAVE_PERMISSION_TO_ACCESS_THIS)
    })
	public ResponseEntity<Page<TagDTO>> findAll(Pageable pageable);

	@ApiOperation(value = "Update tag by a given id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = SUCCESS_TAG_UPDATE_IN_THE_SYSTEM),
            @ApiResponse(code = 400, message = MISSING_REQUIRED_FIELDS_OR_WRONG_FIELD_RANGE_VALUE),
            @ApiResponse(code = 403, message = FORBIDDEN_DON_T_HAVE_PERMISSION_TO_ACCESS_THIS),
            @ApiResponse(code = 404, message = TAG_WITH_GIVEN_NAME_NOT_FOUND),
            @ApiResponse(code = 409, message = DATA_INTEGRITY_VIOLATION_TAG_IS_ALREADY_REGISTRED)
    })
	public ResponseEntity<TagDTO> update(Long id, TagDTO obj);
	
	@ApiOperation(value = "Delete a tag found by a given valid Id")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = SUCCESS_TAG_DELETED_IN_THE_SYSTEM),
            @ApiResponse(code = 403, message = FORBIDDEN_DON_T_HAVE_PERMISSION_TO_ACCESS_THIS),
            @ApiResponse(code = 404, message = TAG_WITH_GIVEN_NAME_NOT_FOUND)
    })
	public ResponseEntity<TagDTO> delete(Long id);
}
