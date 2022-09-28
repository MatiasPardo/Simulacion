import com.google.gson.JsonObject;
import com.mercadopago.MercadoPago;
import com.mercadopago.MercadoPago.SDK;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.User;

public class MP {
	public static void main( String[] args ) throws MPException{

		MercadoPago.SDK.configure("APP_USR-1699957472917890-022822-e5a65bb4e341e65ace1429e55fa7e718__LB_LC__-18688378");
		
   		JsonObject json = new JsonObject();
   		json.addProperty("site_id", "MLA");
   		User.find();
//   		User user = new User();
   		System.out.print(SDK.Post("/users/test_user", json).getStringResponse());
    	// payer{"id":1048152455,"nickname":"TETE4455231","password":"qatest2325","site_status":"active","email":"test_user_87953098@testuser.com"} 
        // seller{"id":1048168727,"nickname":"TETE1216149","password":"qatest2985","site_status":"active","email":"test_user_67469533@testuser.com"}
    }
}
