package deltav.android;

import playn.android.GameActivity;
import playn.core.PlayN;

import deltav.core.DeltaV;

public class DeltaVActivity extends GameActivity {

  @Override
  public void main(){
    PlayN.run(new DeltaV());
  }
}
