package dungeonmania.MovingEntities;

public interface MovingEntitySubject {
    public void registerObserver(MovingEntityObserver o);
    public void removeObserver(MovingEntityObserver o);
    public void notifyObservers();
}
