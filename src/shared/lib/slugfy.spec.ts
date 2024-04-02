import slugfy from './slugfy';


describe('Lib::Slugfy', () => {
  test('should be ok', () => {
    expect(25 ** (1/2)).toBe(5);
  });
    
  test('should slugfy a string with spaces', () => {
    expect(slugfy('hello world')).toBe('hello-world');
  });

  test('should slugfy a string with special characters', () => {
    expect(slugfy('hello world@2&')).toBe('hello-world-2-e-');
  });
});