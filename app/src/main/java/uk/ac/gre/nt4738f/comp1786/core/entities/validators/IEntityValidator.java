package uk.ac.gre.nt4738f.comp1786.core.entities.validators;

public interface IEntityValidator<TEntity> {
    EntityState Validate(TEntity entity);
}
