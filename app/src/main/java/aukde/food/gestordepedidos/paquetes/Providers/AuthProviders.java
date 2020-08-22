package aukde.food.gestordepedidos.paquetes.Providers;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AuthProviders {

    FirebaseAuth mAuth;

    public AuthProviders(){
        mAuth = FirebaseAuth.getInstance();
    }

    public Task<AuthResult> Registro(String email , String password){

        return  mAuth.createUserWithEmailAndPassword(email,password);

    }

    public Task<AuthResult> Login(String email , String password){

        return  mAuth.signInWithEmailAndPassword(email,password);

    }


    public String getId() {
        return mAuth.getCurrentUser().getUid();
    }

    public void Logout(){
        mAuth.signOut();
    }

    public boolean existe_sesion(){
        boolean existe = false;
        if(mAuth.getCurrentUser() !=null){
            existe=true;
        }
        return existe;
    }

}