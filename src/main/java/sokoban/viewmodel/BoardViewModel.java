package sokoban.viewmodel;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableValue;
import sokoban.model.*;
import javafx.beans.binding.LongBinding;

abstract public class BoardViewModel {

    abstract IntegerProperty getWithProperty();
    abstract IntegerProperty getHeightProperty();
    public abstract void loadLevelFromString() ;

}
