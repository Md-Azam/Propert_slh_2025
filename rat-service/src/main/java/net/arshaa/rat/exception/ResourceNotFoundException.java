package net.arshaa.rat.exception;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter @Setter @NoArgsConstructor
@AllArgsConstructor
public class ResourceNotFoundException extends RuntimeException{
      private boolean status ;
      private String message ;
}
