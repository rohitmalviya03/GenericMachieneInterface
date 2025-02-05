/*
Script to update the Favorite list of medication
*/
UPDATE fdaintraopmedications SET IsFavourite=0 WHERE MedicationsID='29';
UPDATE fdaintraopmedications SET IsFavourite=0 WHERE MedicationsID='32';
UPDATE fdaintraopmedications SET IsFavourite=0 WHERE MedicationsID='46';
UPDATE fdaintraopmedications SET IsFavourite=0 WHERE MedicationsID='50';
UPDATE fdaintraopmedications SET IsFavourite=0 WHERE MedicationsID='52';

/*
Script to update the Favorite list of fluids
*/
UPDATE fluid SET IsFavourite=0 WHERE FluidId='3';
UPDATE fluid SET IsFavourite=0 WHERE FluidId='4';
UPDATE fluid SET IsFavourite=0 WHERE FluidId='5';
UPDATE fluid SET IsFavourite=0 WHERE FluidId='6';
UPDATE fluid SET IsFavourite=0 WHERE FluidId='7';
UPDATE fluid SET IsFavourite=0 WHERE FluidId='8';