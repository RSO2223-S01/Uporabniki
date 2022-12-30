package si.fri.rso.skupina1.uporabniki.api.v1.resources;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.headers.Header;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.metrics.annotation.Metered;
import org.eclipse.microprofile.metrics.annotation.Counted;
import si.fri.rso.skupina1.uporabniki.lib.User;
import si.fri.rso.skupina1.uporabniki.services.beans.UserBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

	private Logger log = Logger.getLogger(UserResource.class.getName());

	@Inject
	private UserBean userBean;

	@Context
	protected UriInfo uriInfo;

	@Counted(name = "getUsers_total")
	@Metered(name = "getUsers_rate")
	@Operation(description = "Get all users.", summary = "Get users")
	@APIResponses({
			@APIResponse(responseCode = "200", description = "List of users", content = @Content(schema = @Schema(implementation = User.class, type = SchemaType.ARRAY)), headers = {
					@Header(name = "X-Total-Count", description = "Number of objects in list") }) })
	@GET
	public Response getUsers() {

		List<User> users = userBean.getUserFilter(uriInfo);

		return Response.status(Response.Status.OK).entity(users).build();
	}

	@Counted(name = "getUser_total")
	@Metered(name = "getUser_rate")
	@Operation(description = "Get metadata for an image.", summary = "Get metadata for an image")
	@APIResponses({
			@APIResponse(responseCode = "200", description = "Image metadata", content = @Content(schema = @Schema(implementation = User.class))) })
	@GET
	@Path("/{userId}")
	public Response getUser(
			@Parameter(description = "User ID.", required = true) @PathParam("userId") Integer userId) {

		User user = userBean.getUser(userId);

		if (user == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}

		return Response.status(Response.Status.OK).entity(user).build();
	}

	@Counted(name = "createUser_total")
	@Metered(name = "createUser_rate")
	@Operation(description = "Add user.", summary = "Add user")
	@APIResponses({
			@APIResponse(responseCode = "201", description = "User successfully added."),
			@APIResponse(responseCode = "405", description = "Validation error.")
	})
	@POST
	public Response createUser(
			@RequestBody(description = "DTO object with user data.", required = true, content = @Content(schema = @Schema(implementation = User.class))) User user) {

		if (user.getName() == null || user.getSurname() == null || user.getCity() == null
				|| user.getPostalCode() == null
				|| user.getAddress() == null) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		} else {
			user = userBean.createUser(user);
		}

		return Response.status(Response.Status.CONFLICT).entity(user).build();

	}

	@Counted(name = "putUser_total")
	@Metered(name = "putUser_rate")
	@Operation(description = "Update data for an user.", summary = "Update user")
	@APIResponses({
			@APIResponse(responseCode = "200", description = "User successfully updated.")
	})
	@PUT
	@Path("{userId}")
	public Response putUser(
			@Parameter(description = "User ID.", required = true) @PathParam("userId") Integer userId,
			@RequestBody(description = "DTO object with user data.", required = true, content = @Content(schema = @Schema(implementation = User.class))) User user) {

		user = userBean.putUser(userId, user);

		if (user == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}

		return Response.status(Response.Status.NOT_MODIFIED).build();
	}

	@Counted(name = "deleteUser_total")
	@Metered(name = "deleteUser_rate")
	@Operation(description = "Delete user.", summary = "Delete user")
	@APIResponses({
			@APIResponse(responseCode = "200", description = "User successfully deleted."),
			@APIResponse(responseCode = "404", description = "Not found.")
	})
	@DELETE
	@Path("{userId}")
	public Response deleteUser(
			@Parameter(description = "User ID.", required = true) @PathParam("userId") Integer userId) {

		boolean deleted = userBean.deleteUser(userId);

		if (deleted) {
			return Response.status(Response.Status.NO_CONTENT).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}

}
