// async function uploadImage() {
//   const fileInput = document.getElementById('imageUpload');
//   const file = fileInput.files[0];

//   const metadata = {
//     name: file.name,
//     mimeType: file.type
//   };

//   const keyFile = 'resonant-gizmo-387405-99f6c39a87e2.json'; // Replace with the path to your downloaded JSON key file

//   const formData = new FormData();
//   formData.append('metadata', new Blob([JSON.stringify(metadata)], { type: 'application/json' }));
//   formData.append('file', file);

//   try {
//     const response = await fetch('https://www.googleapis.com/upload/drive/v3/files?uploadType=multipart', {
//       method: 'POST',
//       headers: {
//         Authorization: `Bearer ${keyFile}`,
//       },
//       body: formData
//     });

//     if (response.ok) {
//       console.log('Image uploaded successfully');
//     } else {
//       console.error('Image upload failed');
//     }
//   } catch (error) {
//     console.error('An error occurred during image upload:', error);
//   }
// }
